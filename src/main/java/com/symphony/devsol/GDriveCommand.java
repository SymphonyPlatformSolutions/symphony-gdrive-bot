package com.symphony.devsol;

import com.symphony.bdk.core.activity.ActivityMatcher;
import com.symphony.bdk.core.activity.command.CommandActivity;
import com.symphony.bdk.core.activity.command.CommandContext;
import com.symphony.bdk.core.activity.model.ActivityInfo;
import com.symphony.bdk.core.activity.model.ActivityType;
import com.symphony.bdk.core.service.datafeed.EventException;
import com.symphony.bdk.core.service.message.MessageService;
import com.symphony.bdk.core.service.message.model.Message;
import com.symphony.bdk.core.service.stream.StreamService;
import com.symphony.bdk.core.service.user.UserService;
import com.symphony.bdk.gen.api.model.MemberInfo;
import com.symphony.bdk.gen.api.model.UserV2;
import com.symphony.bdk.gen.api.model.V4Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class GDriveCommand extends CommandActivity<CommandContext> {
    private final Pattern p = Pattern.compile(".*(https://docs.google.com/[^ ]*).*");
    private final MessageService messages;
    private final StreamService streams;
    private final UserService users;
    private final GDriveService gdrive;
    private String url = null;

    @Override
    protected ActivityMatcher<CommandContext> matcher() throws EventException {
        return c -> {
            Matcher m = p.matcher(c.getTextContent());
            if (m.matches()) {
                url = m.group(1);
                return true;
            }
            return false;
        };
    }

    @Override
    protected void onActivity(CommandContext context) {
        String placeholder = "<i>Processing GDrive permissions..</i>";
        V4Message placeholderMsg = messages.send(context.getStreamId(), placeholder);

        boolean isRoom = context.getSourceEvent().getMessage().getStream().getStreamType().equals("ROOM");
        List<Long> members = List.of(context.getInitiator().getUser().getUserId());

        String fileId = gdrive.getFileIdFromUri(url);
        Callable<String> fileNameCall = gdrive.getFileName(fileId);
        Callable<List<String>> permissionsCall = gdrive.getPermissions(fileId);
        Callable<List<Long>> roomMembersCall = () -> streams.listRoomMembers(context.getStreamId()).stream().map(MemberInfo::getId).toList();

        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(fileNameCall);
        service.submit(permissionsCall);
        if (isRoom) {
            service.submit(roomMembersCall);
        }
        service.shutdown();

        String fileName;
        List<String> permissions;
        try {
            fileName = fileNameCall.call();
            permissions = permissionsCall.call();
            if (isRoom) {
                members = roomMembersCall.call();
            }
        } catch (Exception e) {
            log.error("Unable to extract data", e);
            messages.send(context.getStreamId(), "Unable to extract data");
            return;
        }

        List<String> memberEmails = users.listUsersByIds(members, true, true).stream()
            .filter(u -> u.getAccountType() == UserV2.AccountTypeEnum.NORMAL)
            .map(UserV2::getEmailAddress)
            .toList();

        List<String> added = memberEmails.stream().filter(email -> !permissions.contains(email))
            .map(email -> gdrive.addPermission(fileId, email))
            .filter(Objects::nonNull)
            .toList();

        MessageData data = MessageData.builder()
            .title(fileName)
            .url(url)
            .existing(permissions)
            .added(added)
            .build();
        String message = messages.templates()
            .newTemplateFromClasspath("message.ftl").process(data);
        messages.update(placeholderMsg, Message.builder().content(message).build());
    }

    @Override
    protected ActivityInfo info() {
        return new ActivityInfo()
            .name("GDrive Command")
            .type(ActivityType.COMMAND)
            .description("Google Drive permissions fixer");
    }
}

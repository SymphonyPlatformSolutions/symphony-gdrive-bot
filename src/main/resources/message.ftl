<expandable-card state="collapsed">
  <header>
    <b>
      <span
        class="tempo-text-color--blue">G</span><span
        class="tempo-text-color--red">D</span><span
        class="tempo-text-color--yellow">r</span><span
        class="tempo-text-color--blue">i</span><span
        class="tempo-text-color--green">v</span><span
        class="tempo-text-color--red">e</span>:
      <a href="${url}">${title}</a>
    </b>
  </header>
  <body>
  <#list existing>
    <b>Users who already have access:</b>
    <ul>
        <#items as user>
          <li>${user}</li>
        </#items>
    </ul>
  </#list>

  <#list added>
    <br/>
    <b>Users added to this file:</b>
    <ul>
        <#items as user>
          <li>${user}</li>
        </#items>
    </ul>
  </#list>
  </body>
</expandable-card>

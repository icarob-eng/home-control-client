# Home Control Client

App baseado em sockets. Mensagens dos Sockets especificadas na Enum `Msg.java`.

Conexões gerenciadas por `SocketClient.java`, classe instanciada por uma activity e com os
seguintes métodos públicos: `actionReset()`, `actionPing()`, `testConn()`, `actionLED()`,
`actionRelay()`, `actionAddNw()` e `actionRmNw()`.

Ao mesmo tempo, `SocketClient.java` se comunica de volta com a activity que a instnaciou,
printando valores no terminal embutido ou lendo valores com `getIP()` e `getPort()`.

A activity `NetworkSetup.java` pega o valor estático de `SocketClient` salvo na classe e
`MainActivity.java` e acessa os métodos de alteração de network do client.
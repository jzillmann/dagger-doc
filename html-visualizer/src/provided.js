var graphPayload = { // eslint-disable-line no-unused-vars
    "nodes": [
        {
            "name": "AppComponent",
            "type": "COMPONENT"
        },
        {
            "name": "AppModule",
            "type": "MODULE",
            "category": "App Layer"
        },
        {
            "name": "ChatModule",
            "type": "MODULE",
            "category": "Domain Layer"
        },
        {
            "name": "ExecutionModule",
            "type": "MODULE",
            "category": "Infrsatructure Layer"
        },
        {
            "name": "PersistenceModule",
            "type": "MODULE",
            "category": "Infrsatructure Layer"
        },
        {
            "name": "UserModule",
            "type": "MODULE",
            "category": "Domain Layer"
        },
        {
            "name": "MailModule",
            "type": "MODULE",
            "category": "Infrsatructure Layer"
        },
        {
            "name": "ServerModule",
            "type": "MODULE",
            "category": "UI Access Layer"

        },
    ],
    "links": [
        {
            "from": "UserModule",
            "to": "PersistenceModule",
            "type": "DEPENDS_ON"
        },
        {
            "from": "AppModule",
            "to": "ServerModule",
            "type": "DEPENDS_ON"
        },
        {
            "from": "AppModule",
            "to": "MailModule",
            "type": "DEPENDS_ON"
        },
        {
            "from": "UserModule",
            "to": "MailModule",
            "type": "DEPENDS_ON"
        },
        {
            "from": "ExecutionModule",
            "to": "MailModule",
            "type": "DEPENDS_ON"
        },
        {
            "from": "ChatModule",
            "to": "ServerModule",
            "type": "CONTRIBUTES_TO"
        },
        {
            "from": "AppModule",
            "to": "PersistenceModule",
            "type": "DEPENDS_ON"
        },
        {
            "from": "AppComponent",
            "to": "AppModule",
            "type": "DEPENDS_ON"
        },
        {
            "from": "UserModule",
            "to": "ServerModule",
            "type": "CONTRIBUTES_TO"
        },
        {
            "from": "AppComponent",
            "to": "ChatModule",
            "type": "DEPENDS_ON"
        },
        {
            "from": "AppModule",
            "to": "ExecutionModule",
            "type": "DEPENDS_ON"
        },
        {
            "from": "ChatModule",
            "to": "PersistenceModule",
            "type": "DEPENDS_ON"
        },
        {
            "from": "AppComponent",
            "to": "UserModule",
            "type": "DEPENDS_ON"
        }
    ]
}
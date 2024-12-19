let targets = [
    ...document.getElementsByTagName("button"),
    ...document.getElementsByTagName("a")
]

let events = {
    "liked": {
        message: 'Пользователь поставил отметку "Нравится" на видео с id ',
        func: getActionMessageByBaseURI
    },
    "disliked": {
        message: 'Пользователь поставил отметку "Не нравится" на видео с id ',
        func: getActionMessageByBaseURI
    },
    "removed-like": {
        message: 'Пользователь убрал отметку "Нравится" с видео с id ',
        func: getActionMessageByBaseURI
    },
    "removed-dislike": {
        message: 'Пользователь убрал отметку "Не нравится" с видео с id ',
        func: getActionMessageByBaseURI
    },
    "find": {
        message: 'Пользователь искал видео по теме: ',
        func: getFindActionMessage
    },
    "video": {
        message :'Пользователь перешел на страницу видео с id ',
        func: getActionMessageByHref
    },
    "thumbnail": {
        message :'Пользователь перешел на страницу видео с id ',
        func: getActionMessageByThumbnail
    },
    "to-videos-list": {
        message: 'Пользователь перешел по "<-- К списку видео"',
        func: getActionMessage
    }
}

targets.forEach(target => {
    target.addEventListener("click", (e) => {
        let actionMsg = getActionMsg(e);
        if (actionMsg !== null) {
            let log = generateLog(actionMsg);
            sendLog(log);
            console.log(log)
        }
    })
})

function generateLog(actionMsg){
    return {
        action: actionMsg,
        datetime: new Date()
    };
}

function getActionMsg(e) {
    let target = e.target;
    let event = target.className;
    let message = events[event].message;
    let func = events[event].func;
    return func(e, message)
}

function getActionMessageByBaseURI(e, message) {
    let target = e.target;
    return message + target.baseURI.replace("http://localhost:8081/user/videos/", "")
}

function getActionMessageByHref(e, message) {
    console.log(e)
    let target = e.target;
    return message + target.href.replace("http://localhost:8081/user/videos/", "")
}

function getFindActionMessage(e, message) {
    let select = document.querySelector(".select-tag");
    return message + select.options[select.selectedIndex].text;
}

function getActionMessageByThumbnail(e, message) {
    return message + e.target.parentElement.href.replace("http://localhost:8081/user/videos/", "")
}

function getActionMessage(e, message) {
    return message;
}

async function sendLog(log) {
    await fetch("/user/logs", {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(log)
    })
}


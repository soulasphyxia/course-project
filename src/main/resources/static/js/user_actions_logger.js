let targets = [
    ...document.getElementsByTagName("button"),
    ...document.getElementsByTagName("a")
]

let events = {
    "liked": 'Пользователь поставил отметку "Нравится" на видео с id ',
    "disliked": 'Пользователь поставил отметку "Не нравится" на видео с id ',
    "removed-like": 'Пользователь убрал отметку "Нравится" с видео с id ',
    "removed-dislike": 'Пользователь убрал отметку "Не нравится" с видео с id ',
    "find": 'Пользователь искал видео по теме: ',
    "video": 'Пользователь перешел на страницу видео с id ',
    "to-videos-list": 'Пользователь перешел по "<-- К списку видео"'
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
    let dateObj = new Date();
    dateObj.getTime().toLocaleString();
    return {
        action: actionMsg,
        date: dateObj.toLocaleDateString("ru"),
        time: dateObj.toLocaleTimeString("ru")
    };
}

function getActionMsg(e) {
    let target = e.target;
    let event = target.className;
    switch (event) {
        case 'liked':
        case "disliked":
        case "removed-like":
        case "removed-dislike":
            let btnVideoId = target.baseURI.replace("http://localhost:8081/user/videos/", "")
            return events[event] + btnVideoId;
        case "video":
            let AvideoId = target.href.replace("http://localhost:8081/user/videos/", "")
            return events[event] + AvideoId;
        case "find":
            let pattern = document.querySelector("#pattern")
            if (pattern.value.trim().length > 0) {
                return events[event] + `"${pattern.value}"`;
            }
            break;
        case "to-videos-list":
            return events[event];
    }
    return null;
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


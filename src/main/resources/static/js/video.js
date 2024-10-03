const likeBtn = document.getElementById("like-btn");
const dislikeBtn = document.getElementById("dislike-btn");
const videoId = document.URL.replace("http://localhost:8081/user/videos/", "");
const likes = document.getElementById("likes");
const dislikes = document.getElementById("dislikes");

function initBtnStates() {
    if(localStorage.getItem("userLikes") == null) {
        localStorage.setItem("userLikes", JSON.stringify({}));
    }
    let userLikes = JSON.parse(localStorage.getItem("userLikes"));
    if (userLikes[videoId] == null) {
        userLikes = setBtnState(false, false);
    }
    let likeState = userLikes[videoId]["like"];
    let dislikeState = userLikes[videoId]["dislike"];

    if (!likeState && !dislikeState) {
        likeBtn.className = "removed-like";
        dislikeBtn.className = "removed-dislike"
    } else if (likeState && !dislikeState) {
        likeBtn.className = "liked";
        dislikeBtn.className = "blocked";
    } else {
        likeBtn.className = "blocked";
        dislikeBtn.className = "disliked"
    }
}

initBtnStates();

likeBtn.addEventListener("click", () => {
    const className = likeBtn.className;
    if(className === "liked") {
        likeBtn.className = "removed-like";
        removeLike();
        decreaseValue(likes, likes.textContent)
        dislikeBtn.className = "removed-dislike"
        setBtnState(false, false)
    } else if (className === "blocked"){
        likeBtn.className = "liked";
        like();
        increaseValue(likes, likes.textContent)
        decreaseValue(dislikes, dislikes.textContent);
        removeDislike();
        dislikeBtn.className = "blocked";
        setBtnState(true, false)
    } else if (className === "removed-like") {
        likeBtn.className = "liked";
        like();
        increaseValue(likes, likes.textContent);
        dislikeBtn.className = "blocked";
        setBtnState(true, false)
    }
})

dislikeBtn.addEventListener("click", () => {
    const className = dislikeBtn.className;
    if(className === "disliked") {
        dislikeBtn.className = "removed-dislike";
        removeDislike();
        decreaseValue(dislikes, dislikes.textContent)
        likeBtn.className = "removed-like"
        setBtnState(false, false)
    } else if (className === "blocked"){
        dislikeBtn.className = "disliked";
        dislike();
        increaseValue(dislikes, dislikes.textContent)
        decreaseValue(likes, likes.textContent);
        removeLike();
        likeBtn.className = "blocked";
        setBtnState(false, true)
    } else if (className === "removed-dislike") {
        dislikeBtn.className = "disliked";
        dislike();
        increaseValue(dislikes, dislikes.textContent);
        likeBtn.className = "blocked";
        setBtnState(false, true)
    }
})

function increaseValue(element, value) {
    element.textContent = (parseInt(value) + 1).toString();
}

function decreaseValue(element, value) {
    element.textContent = (parseInt(value) - 1).toString();
}

function setBtnState(likeState, dislikeState) {
    const userLikes = JSON.parse(localStorage.getItem("userLikes"))
    userLikes[videoId] = {
        "like": likeState,
        "dislike": dislikeState
    }
    localStorage.setItem("userLikes", JSON.stringify(userLikes));
    return userLikes
}


async function like() {
    await fetch(`/user/like/videos/${videoId}`, {
        method: 'POST'
    })
}

async function removeLike() {
    await fetch(`/user/remove-like/videos/${videoId}`, {
        method: 'POST'
    })
}

async function removeDislike() {
    await fetch(`/user/remove-dislike/videos/${videoId}`, {
        method: 'POST'
    })
}

async function dislike() {
    await fetch(`/user/dislike/videos/${videoId}`, {
        method: 'POST'
    })
}


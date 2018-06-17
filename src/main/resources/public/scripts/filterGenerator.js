function deleteFilter(id) {
    var elem = document.getElementById(id);
    elem.parentNode.removeChild(elem);
    updateSlice(idSlice)
    return false;
}


function getRootDiv(id){
    var rootDiv = document.createElement('div');
    rootDiv.classList.toggle("level")
    rootDiv.classList.toggle("imgFilter")
    rootDiv.classList.toggle(id)
    rootDiv.id = id
    return rootDiv
}

function getSliderLable(id, filterName) {
    var levelItem = document.createElement('div')
    levelItem.classList.toggle("level-item")
    levelItem.classList.toggle(id)
    levelItem.innerHTML += '<div><p><b>' + filterName +':</b></p></div>'

    return levelItem
}

function getTextLevelDiv(id, text) {
    var levelItem = document.createElement('div')
    levelItem.classList.toggle("level-item")
    levelItem.classList.toggle("has-text-centered")
    levelItem.classList.toggle(id)
    levelItem.innerHTML += '<div><p>' + text + '</b></p></div>'

    return levelItem
}

function getSliderDiv(id, filterId, max) {
    var levelItem = document.createElement('div');
    levelItem.classList.toggle("level-item");
    levelItem.classList.toggle(id)

    var sliderItem = document.createElement('input')
    sliderItem.className = "filterSlider slider has-output-tooltip is-fullwidth"
    sliderItem.id = filterId
    sliderItem.min = 0
    sliderItem.max = max
    sliderItem.step = 1
    sliderItem.type = "range"
    sliderItem.setAttribute("value", "0")

    var outputItem = document.createElement('output')
    outputItem.setAttribute("for", filterId)
    outputItem.innerText = 0

    var childDiv = document.createElement('div')
    childDiv.appendChild(sliderItem)
    childDiv.appendChild(outputItem)
    levelItem.appendChild(childDiv)

    return levelItem
}


function getButtonDiv(id) {
    var levelItem = document.createElement('div');
    levelItem.classList.toggle("level-item");
    levelItem.classList.toggle(id)

    var bottonItem = document.createElement('a');
    bottonItem.className = "button is-danger is-outlined"
    bottonItem.innerHTML += '<span>Delete</span><span class="icon is-small"><i class="fas fa-times"></i></span>'
    bottonItem.for=id
    bottonItem.addEventListener("click", function(event) {
                    deleteFilter(event.currentTarget.for)
                });

    levelItem.appendChild(bottonItem)
    return levelItem
}

function getMax(filterName) {
    if (filterName == "Threshold") return 255
    return Math.floor(shapes['width']/10)
}

function addFilter(filterName) {
    var id = Math.random().toString(36).substring(7);
    var filterId = filterName + "-" + id
    var max = getMax(filterName)


    var rootDiv = getRootDiv(id)

    rootDiv.appendChild(getSliderLable(id, filterName))
    rootDiv.appendChild(getTextLevelDiv(id, 0))
    rootDiv.appendChild(getSliderDiv(id, filterId, max))
    rootDiv.appendChild(getTextLevelDiv(id, max))
    rootDiv.appendChild(getButtonDiv(id))


    var select = document.getElementById('SlicesOpts');
    select.appendChild(rootDiv)


    sliderItem = document.getElementById(filterId)
        sliderItem.addEventListener("change", function(event) {
                updateSlice(idSlice)
            }, false);

    updateSliderUi()

}
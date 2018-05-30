function deleteFilter(id) {
    var elem = document.getElementById(id);
    elem.parentNode.removeChild(elem);
    return false;
}

function addFilter(filterName) {
    var id = Math.random().toString(36).substring(7);
    var rootDiv = document.createElement('div');
    rootDiv.classList.toggle("level")
    rootDiv.classList.toggle("imgFilter")
    rootDiv.classList.toggle(id)
    rootDiv.id = id

    var levelItem = document.createElement('div')
    levelItem.classList.toggle("level-item")
    levelItem.classList.toggle(id)
    rootDiv.innerHTML += '<div><p><b>' + filterName +':</b></p></div>'


    levelItem = document.createElement('div')
    levelItem.classList.toggle("level-item")
    levelItem.classList.toggle(id)
    rootDiv.innerHTML += '<div><p>0</b></p></div>'


    levelItem = document.createElement('div');
    levelItem.classList.toggle("level-item");
    levelItem.classList.toggle(id)

    var filterId = filterName + "-" + id
    var sliderItem = document.createElement('input')
    sliderItem.className = "imgSlider filterSlider slider has-output-tooltip is-fullwidth"
    sliderItem.id = filterId
    sliderItem.min = 0
    sliderItem.max = shapes['width']
    sliderItem.step = 1
    sliderItem.type = "range"
    sliderItem.setAttribute("value", "0")
    sliderItem.addEventListener("change", function(event) {
        updateSlice(event.currentTarget.value)
    });

    var outputItem = document.createElement('output')
    outputItem.setAttribute("for", filterId)
    outputItem.innerText = 0

    var childDiv = document.createElement('div')
    childDiv.appendChild(sliderItem)
    childDiv.appendChild(outputItem)
    levelItem.appendChild(childDiv)
    rootDiv.appendChild(levelItem)



    levelItem = document.createElement('div');
    levelItem.classList.toggle("level-item");
    levelItem.classList.toggle(id)
    rootDiv.innerHTML += '<div><p>'+ shapes['width'] +'</p></div>'


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
    rootDiv.appendChild(bottonItem)


    var select = document.getElementById('optionsColumn');
    select.appendChild(rootDiv)

}
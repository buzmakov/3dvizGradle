

function changeVizType(type) {
    var vizArea = type + "Viz"
    var vizAreas = document.getElementsByClassName('vizArea')
    for (var i = 0; i < vizAreas.length; i++) {
        if(vizAreas[i].id == vizArea) {
            vizAreas[i].style.display = "block";
            currVizType = type
        } else {
            vizAreas[i].style.display = "none";
        }
    }
}

function changeActiveBotton(obj) {
    var vizTypeBottons = document.getElementsByClassName('viz-type')
    for (var i = 0; i < vizTypeBottons.length; i++) {
        vizTypeBottons[i].classList.remove("is-active");
        vizTypeBottons[i].classList.remove("is-primary");
    }

    obj.classList.toggle("is-active");
    obj.classList.toggle("is-primary");

}

function init() {
    var x = document.getElementsByClassName("dropdown");
    for (var i = 0; i < x.length; i++) {
        x[i].addEventListener('click', function(event) {
            event.stopPropagation();
            event.currentTarget.classList.toggle('is-hoverable');
        });
    }

    var dropdownElements = document.getElementsByClassName('filter-dropdown-item')
    for (var i = 0; i < dropdownElements.length; i++) {
        var objName = dropdownElements[i].innerText
        dropdownElements[i].addEventListener('click', function(event) {
            event.stopPropagation();
            addFilter(event.currentTarget.innerText)
        });
    }

    var imgSliders = document.getElementsByClassName('imgSlider')
    for (var i = 0; i < imgSliders.length; i++) {
        imgSliders[i].addEventListener('change', function(event) {
            idSlice = event.currentTarget.value
            updateSlice(idSlice)
        }, false);
    }

    var vizTypeBottons = document.getElementsByClassName('viz-type')
    for (var i = 0; i < vizTypeBottons.length; i++) {
        vizTypeBottons[i].addEventListener('click', function(event) {
            var vizType = event.currentTarget.innerText
            changeActiveBotton(event.currentTarget)
            changeVizType(vizType)
        }, false);
    }

}

init()
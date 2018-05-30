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

}

init()
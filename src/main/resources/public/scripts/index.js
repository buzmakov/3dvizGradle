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
    for (var i = 0; i < dropdownElements.length; i++) {
        dropdownElements[i].addEventListener('change', function(event) {
            updateSlice(event.currentTarget.value)
        }, false);
    }

}

init()
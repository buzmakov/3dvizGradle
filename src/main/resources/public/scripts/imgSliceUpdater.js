function updateSliceSliderValue() {
    var req = getXmlHttp()
    req.open("GET", host + "/objects/current/shape/" + "?r=" + Math.random(), true);
    req.onreadystatechange = function() {
        if (req.readyState == 4) {
           if(req.status == 200) {
                var dataShape = JSON.parse(req.responseText)
                shapes = dataShape

                var sliderSlice = document.getElementById('sliderSlice');
                var sliderSliceMin = document.getElementById('sliceMin');
                var sliderSliceMax = document.getElementById('sliceMax');

                sliderSlice.min = 0
                sliderSliceMin.innerText = 0

                sliderSlice.max = dataShape["num"]
                sliderSliceMax.innerText = dataShape["num"]
        	 }
        }
    };
    req.send(null);
}


function generateFIlterBody() {
    var filterArr = []
    var imgFilterSliders = document.getElementsByClassName('filterSlider')
    for (var i = 0; i < imgFilterSliders.length; i++) {
        var filterName = imgFilterSliders[i].id.split("-")[0]
        filterArr.push({filterName : imgFilterSliders[i].value})
    }

    return filterArr
}


function updateSlice(id) {
    var filters = generateFIlterBody()

    req.open("POST", host + "/objects/current/slice/filters/" + "?r=" + Math.random(), true);
    req.onreadystatechange = function() {
        if (req.readyState == 4) {
           if(req.status == 200) {
                var outputImg = document.getElementById('viz-img');
                outputImg.src = host + "/objects/current/slice/"+ id +"/?r=" + Math.random();
        	 }
        }
    };

    req.send(null);

    var outputImg = document.getElementById('viz-img');
    outputImg.src = host + "/objects/current/slice/"+ id +"/?r=" + Math.random();
}
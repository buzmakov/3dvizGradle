




function reqConvertObj(objName) {
    var req = getXmlHttp()

    req.open("POST", host + "/objects/current/" + "?r=" + Math.random(), true);
    req.onreadystatechange = function() {
          if (req.readyState == 4) {
             if(req.status == 200) {
                updateSlice(0);
                updateSliceSliderValue()
        	 }
          }
        };

    req.send("{\"objName\": \"" + objName + "\"}");
}


function changeObjectEvent(objName) {
     var select = document.getElementById('current-object-name');
     select.innerText = "Current object: " + objName

     reqConvertObj(objName)

}




function updateDropDownByList(objList) {
    var select = document.getElementById('objList-dropdown-content');

    var options = [];
    var option = document.createElement('a');
    option.classList.toggle('dropdown-item')
    option.classList.toggle('objList-dropdown-item')

    for (var i = 0; i < objList.length; i++) {
        option.text = option.value = objList[i];
        options.push(option.outerHTML);
    }

    select.insertAdjacentHTML('beforeEnd', options.join('\n'));

    var dropdownElements = document.getElementsByClassName('objList-dropdown-item')
        for (var i = 0; i < dropdownElements.length; i++) {
            var objName = dropdownElements[i].innerText
            dropdownElements[i].addEventListener('click', function(event) {
                event.stopPropagation();
                changeObjectEvent(event.currentTarget.innerText)
            });
        }
}




function updateObjList(type, url) {
    var req = getXmlHttp()
    req.open(type, host + url + "?r=" + Math.random(), true);
    req.onreadystatechange = function() {
      if (req.readyState == 4) {
         if(req.status == 200) {
            updateDropDownByList(JSON.parse(req.responseText));
    	 }
      }
    };
    req.send(null);
}

updateObjList('GET', '/objects/all/');
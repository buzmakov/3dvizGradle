var host = "http://localhost:4567"

function getXmlHttp(){
  var xmlhttp;
  try {
    xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
  } catch (e) {
    try {
      xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    } catch (E) {
      xmlhttp = false;
    }
  }
  if (!xmlhttp && typeof XMLHttpRequest!='undefined') {
    xmlhttp = new XMLHttpRequest();
  }
  return xmlhttp;
}


function updateSliceSliderValue(dataShape) {

}


function updateSlice(id) {
    var outputImg = document.getElementById('viz-img');
    outputImg.src = host + "/objects/current/slice/"+ id +"/";
}


function reqConvertObj(objName) {
    var req = getXmlHttp()

    //set object in server
    req.open("POST", host + "/objects/current/" + "?r=" + Math.random(), true);
    req.onreadystatechange = function() {
          if (req.readyState == 4) {
             if(req.status == 200) {
                updateSlice(0);
        	 }
          }
        };

    req.send("{\"objName\": \"" + objName + "\"}");

    req.open("GET", host + "/objects/current/shape/" + "?r=" + Math.random());
    req.send(null);

    var dataShape = JSON.parse(req.responseText)

    updateSliceSliderValue(dataShape)

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




function init() {
    updateObjList('GET', '/objects/all/');

    var dropdown = document.getElementById('objList-dropdown');
    dropdown.addEventListener('click', function(event) {
      event.stopPropagation();
      dropdown.classList.toggle('is-active');
    });
}

init()















// Find output DOM associated to the DOM element passed as parameter
function findOutputForSlider( element ) {
   var idVal = element.id;
   outputs = document.getElementsByTagName( 'output' );
   for( var i = 0; i < outputs.length; i++ ) {
     if ( outputs[ i ].htmlFor == idVal )
       return outputs[ i ];
   }
}

function getSliderOutputPosition( slider ) {
  // Update output position
  var newPlace,
      minValue;

  var style = window.getComputedStyle( slider, null );
  // Measure width of range input
  sliderWidth = parseInt( style.getPropertyValue( 'width' ), 10 );

  // Figure out placement percentage between left and right of input
  if ( !slider.getAttribute( 'min' ) ) {
    minValue = 0;
  } else {
    minValue = slider.getAttribute( 'min' );
  }
  var newPoint = ( slider.value - minValue ) / ( slider.getAttribute( 'max' ) - minValue );

  // Prevent bubble from going beyond left or right (unsupported browsers)
  if ( newPoint < 0 ) {
    newPlace = 0;
  } else if ( newPoint > 1 ) {
    newPlace = sliderWidth;
  } else {
    newPlace = sliderWidth * newPoint;
  }

  return {
    'position': newPlace + 'px'
  }
}

document.addEventListener( 'DOMContentLoaded', function () {
  // Get all document sliders
  var sliders = document.querySelectorAll( 'input[type="range"].slider' );
  [].forEach.call( sliders, function ( slider ) {
    var output = findOutputForSlider( slider );
    if ( output ) {
      if ( slider.classList.contains( 'has-output-tooltip' ) ) {
        // Get new output position
        var newPosition = getSliderOutputPosition( slider );

        // Set output position
        output.style[ 'left' ] = newPosition.position;
      }

      // Add event listener to update output when slider value change
      slider.addEventListener( 'input', function( event ) {
        if ( event.target.classList.contains( 'has-output-tooltip' ) ) {
          // Get new output position
          var newPosition = getSliderOutputPosition( event.target );

          // Set output position
          output.style[ 'left' ] = newPosition.position;
        }

        // Update output with slider value
        output.value = event.target.value;
      } );
    }
  } );
} );
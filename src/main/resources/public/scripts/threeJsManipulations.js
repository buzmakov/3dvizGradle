var container, stats;

var camera, scene, renderer, model, particleMaterial, zoom, rotate, controls, id;

var mouseX = 0, mouseY = 0;

var width = 400;
var height = 700;

var windowHalfX = width / 2;
var windowHalfY = height / 2;


function createScene() {
    scene = new THREE.Scene();

    var ambient = new THREE.AmbientLight(  0x202020 );
    scene.add( ambient );

    var directionalLight = new THREE.DirectionalLight( 0xffffff, 0.75 );
    directionalLight.position.set( 0, 0, 1 );
    scene.add( directionalLight );

	scene.background = new THREE.Color( 0xf0f0f0 );

    var helper = new THREE.GridHelper( 2000, 100 );
    helper.position.y = - 199;
    helper.material.opacity = 0.25;
    helper.material.transparent = true;
    scene.add( helper );

	return scene;
}


function destroyScene() {
    container = document.getElementById('SurfacesViz');
    while (container.firstChild) {
        container.removeChild(container.firstChild);
    }
    cancelAnimationFrame(id);
    renderer = null;
    scene = null;
    projector = null;
    camera = null;
    controls = null;
}

function initScene() {
    destroyScene()
    container = document.getElementById('SurfacesViz');
    width = container.clientWidth


    camera = new THREE.PerspectiveCamera( 45, width / height, 1, 2000 );
    camera.position.z = 50;
    camera.position.x = 50;
    camera.lookAt(0,0,0)
    controls = new THREE.OrbitControls( camera );

    createScene()

    // model
    var manager = new THREE.LoadingManager();

    var onProgress = function ( xhr ) {
    	if ( xhr.lengthComputable ) {
    		var percentComplete = xhr.loaded / xhr.total * 100;
    		console.log( Math.round(percentComplete, 2) + '% downloaded' );
    	}
    };

    var onError = function ( xhr ) {
    };


    var loader = new THREE.OBJLoader( manager );
    loader.load( host + "/objects/current/objFile/" + "?r=" + Math.random(), function ( object ) {
    	console.log(object);

    	object.position.x = 0;
    	object.position.y = 0;
    	object.position.z = 0;
        scene.add( object );
    }, onProgress, onError );



    renderer = new THREE.WebGLRenderer();
    renderer.setPixelRatio( window.devicePixelRatio );
    renderer.setSize( width, height );

    var ctx = renderer.context;
    ctx.getShaderInfoLog = function () { return '' };

    container.appendChild( renderer.domElement );

    window.addEventListener( 'resize', onWindowResize, false );
    animate()
}


function onWindowResize() {
	windowHalfX = width / 2;
	windowHalfY = height / 2;
	camera.aspect = width / height;
	camera.updateProjectionMatrix();
	renderer.setSize(width, height);
}

function onDocumentMouseMove( event ) {
	mouseX = ( event.clientX - windowHalfX ) / 2;
	mouseY = ( event.clientY - windowHalfY ) / 2;
}

function animate() {
	id = requestAnimationFrame( animate );
	render();
}

function render() {
	renderer.render( scene, camera );
}



function zoomIn(){
  camera.fov -= 1;
  camera.updateProjectionMatrix();
  zoom = camera.fov;
}

function zoomOut(){
  camera.fov += 1;
  camera.updateProjectionMatrix();
  zoom = camera.fov;
}

function rotateOn(){
  rotate = true;
}

function rotateOff(){
  rotate = false;
}

function wireframeOn(){
  mesh.material.wireframe = true;
  mesh.material.color = new THREE.Color( 0x6893DE  );
}

function wireframeOff(){
  mesh.material.wireframe = false;
  mesh.material.color = new THREE.Color(0xffffff);
}

function front(){
  scene.remove(camera);
  addCamera(0, -6, 1);
  mesh.rotation.z = 0;
  camera.fov = zoom;
  camera.updateProjectionMatrix();
}

function back(){
  front();
  mesh.rotation.z = 3.2;
}

function left(){
  front();
  mesh.rotation.z = 4.8;
}

function right(){
  front();
  mesh.rotation.z = 1.6;
}

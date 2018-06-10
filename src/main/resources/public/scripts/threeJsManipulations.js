            var container, stats;

            var camera, scene, renderer, model, particleMaterial, zoom, rotate;

            var mouseX = 0, mouseY = 0;

            var width = 400;
            var height = 700;

            var windowHalfX = width / 2;
            var windowHalfY = height / 2;


            init();
            animate();


            function init() {

                container = document.getElementById('obj');

                scene = new THREE.Scene();

                addCamera(0, -6, 1);


                zoom = camera.fov;

                var ambient = new THREE.AmbientLight(  0x202020 );
                scene.add( ambient );

                var directionalLight = new THREE.DirectionalLight( 0xffffff, 0.75 );
                directionalLight.position.set( 0, 0, 1 );
                scene.add( directionalLight );

                var pointLight = new THREE.PointLight( 0xffffff, 5, 29 );
                pointLight.position.set( 0, -25, 10 );
				        scene.add( pointLight );

                var loader = new THREE.OBJLoader();
                loader.load( "img/exampleObj.obj", function ( object ) {
                  object.children[0].geometry.computeFaceNormals();
                  var  geometry = object.children[0].geometry;
				          console.log(geometry);
                  THREE.GeometryUtils.center(geometry);
                  geometry.dynamic = true;
                  var material = new THREE.MeshLambertMaterial({color: 0xffffff, shading: THREE.FlatShading, vertexColors: THREE.VertexColors });
                  mesh = new THREE.Mesh(geometry, material);
                  scene.add( mesh );
                } );

                renderer = new THREE.WebGLRenderer();
                renderer.setSize( width, height );
                container.appendChild( renderer.domElement );
            }

            function animate() {
                requestAnimationFrame( animate );
                render();
            }

            function render() {
                if(rotate)
                    mesh.rotation.z += 0.025;

                camera.lookAt( scene.position );
                renderer.render( scene, camera );
            }

            function addCamera(x, y, z){
                camera = new THREE.PerspectiveCamera( 17, width / height, 1, 4000 );
                camera.position.x = x;
                camera.position.y = y;
                camera.position.z = z;
                scene.add( camera );
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
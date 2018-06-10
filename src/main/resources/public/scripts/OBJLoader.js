/**
 * @author mrdoob / http://mrdoob.com/
 */

THREE.OBJLoader = function () {};

THREE.OBJLoader.prototype = new THREE.Loader();
THREE.OBJLoader.prototype.constructor = THREE.OBJLoader;

THREE.OBJLoader.prototype.load = function ( url, callback ) {
	var that = this;
	var xhr = new XMLHttpRequest();

	xhr.onreadystatechange = function () {
		if ( xhr.readyState == 4 ) {
			if ( xhr.status == 200 || xhr.status == 0 ) {
				callback( that.parse( xhr.responseText ) );
			} else {
				console.error( 'THREE.OBJLoader: Couldn\'t load ' + url + ' (' + xhr.status + ')' );
			}
		}
	};

	xhr.open( "GET", url, true );
	xhr.send( null );
};

THREE.OBJLoader.prototype.parse = function ( data ) {
	function vector( x, y, z ) {
		return new THREE.Vector3( x, y, z );
	}
	function face3( a, b, c, normals ) {
		return new THREE.Face3( a, b, c, normals );
	}

	var group = new THREE.Object3D();

	var vertices = [];
	var normals = [];

	var pattern, result;

	// v float float float
	pattern = /v( [\d|\.|\+|\-|e]+)( [\d|\.|\+|\-|e]+)( [\d|\.|\+|\-|e]+)/g;
	while ( ( result = pattern.exec( data ) ) != null ) {
		// ["v 1.0 2.0 3.0", "1.0", "2.0", "3.0"]
		vertices.push( vector(
			parseFloat( result[ 1 ] ),
			parseFloat( result[ 2 ] ),
			parseFloat( result[ 3 ] )
		) );
	}

	var data = data.split( '\no ');

	for ( var i = 0, l = data.length; i < l; i ++ ) {
		var object = data[ i ];
		var geometry = new THREE.Geometry();
		geometry.vertices = vertices;

		// f vertex vertex vertex
		pattern = /f( [\d]+)( [\d]+)( [\d]+)?/g;
		while ( ( result = pattern.exec( object ) ) != null )
			// ["f 1 2 3", "1", "2", "3"]
			geometry.faces.push( face3(
				parseInt( result[ 1 ] ) - 1,
				parseInt( result[ 2 ] ) - 1,
				parseInt( result[ 3 ] ) - 1,
			) );
		}

		geometry.computeCentroids();
		group.add( new THREE.Mesh( geometry, new THREE.MeshLambertMaterial() ) );

	}

	return group;
}

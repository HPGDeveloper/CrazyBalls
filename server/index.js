var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = []; //Jugadores en el juego
var room = 0; //Nivel en el que se empieza
server.listen(8080, function(){
	console.log("Server is now running...");
});

io.on('connection', function(socket){
	socket.join(room); //Cuando nos conectamos nos unimos al nivel 0
	console.log("Player Connected: " + socket.id + " in room " + room);
	socket.emit('socketID', { id: socket.id}); //Ejecutamos la funcion 'socketID' de la clase GameScreen
	socket.emit('getPlayers', players); //Ejecutamos la funcion 'getPlayers' de la clase GameScreen (Obtenemos los jugadores del nivel0)
	/**
	* Cuando tenemos nuestra textura la añadimos
	* y anunciamos que hemos entrado al nivel
	**/
	socket.on('playerTexture', function(data){
		data.id = socket.id;
		for(var i = 0; i < players.length; i++){
			if( players[i].id == data.id){
				players[i].texture = data.texture;
			}
		}
		socket.broadcast.to(room).emit('newPlayer', { id: socket.id, texture: data.texture}); //Avisamos a todos los jugadores del nivel0 que hemos entrado
	});

	/**Cuando nos movemos**/
	socket.on('playerMoved', function(data){
		data.id = socket.id;
		socket.broadcast.to(data.room).emit('playerMoved', data);//Anunciamos a todos los jugadores de nuestro nivel que nos hemos movido
		for(var i = 0; i < players.length; i++){
			if( players[i].id == data.id){
				players[i].x = data.x;
				players[i].y = data.y;
			}
		}
	});
	/**Cuando finalizamos el nivel**/
	socket.on('finishLevel', function(data){
			var texture = "ball.png";
    		data.id = socket.id;
    		socket.broadcast.to(data.level).emit('finishLevel', {id: socket.id});//Avisamos a los jugadores de nuestro nivel que nos vamos
    		socket.leave(data.level);//abandonamos ese nivel(room) para no enviarles mas mensajes
    		console.log("Player: " + socket.id + " finish level" + data.level);
    		for(var i = 0; i < players.length; i++){
    			if( players[i].id == data.id){
    				players[i].level = data.level + 1;
    				texture = players[i].texture;
    			}
    		}
    		socket.emit('getPlayers', players); //Obtenemos los jugadores del nuevo nivel
    		socket.join(data.level + 1);//Nos unimos a la nueva room para enviarles mensajes
    		socket.broadcast.to(data.level + 1).emit('newPlayer', { id: socket.id, texture: texture });//Anunciamos a todos los jugadores del nuevo nivel que hemos entrado
    		console.log("Player: " + socket.id + " join to level" + (data.level + 1));
    	});
    	/**Cuando nos encontramos en una pared activadora**/
	socket.on('activatorWallCollision', function(data){
    	socket.broadcast.to(data.level).emit('activatorWallCollision', data);//Decimos a todos los jugadores del nivel que estamos en una pared activadora
    	});
    	/**Cuando nos desconectamos**/
	socket.on('disconnect', function(){
		console.log("Player Disconnected: " + socket.id);
		for(var i = 0; i < players.length; i++){
			console.log("Number of players before: " + players.length);
			if(players[i].id == socket.id){

					socket.broadcast.to(players[i].level).emit('playerDisconnected', {id: socket.id});
					socket.leave(players[i].level);
					players.splice(i, 1);
					console.log("Number of players after: " + players.length + " " + i + " disconnected");
			}
		}
		socket.broadcast.emit('PlayersOnline', {online: players.length});
	});
    players.push(new player(socket.id, 1.5, 7.5, 0, "ball.png"));//Añadimos el nuevo jugador al vector de jugadores
    socket.broadcast.emit('PlayersOnline', {online: players.length});//Enviamos a todos que nos hemos conectado al juego
    socket.emit('PlayersOnline', {online: players.length});//Ejecutamos el metodo PlayersOnline para ver cuantos jugadores hay
});

function player(id, x, y, level, texture){
	console.log("Player Created id:" + id + "position: " + x + ", " + y + "level: " + level + "textura: " + texture);
	this.id = id;
	this.x = x;
	this.y = y;
	this.level = level;
	this.texture = texture;
}

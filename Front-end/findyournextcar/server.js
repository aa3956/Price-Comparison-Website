//Import the express and url modules
var express = require('express');
var url = require("url");

//Status codes defined in external file
require('./http_status.js');

//The express module is a function. When it is executed it returns an app object
var app = express();

//Import the mysql module
var mysql = require('mysql');

//Create a connection object with the user details
var connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "root",
    password: "",
    database: "findyournextcar",
    debug: false
});

app.use(express.static('public'));

//Set up the application to handle GET requests sent to the user path
app.get('/cars/*', handleGetRequest);//Subfolders
app.get('/cars', handleGetRequest);

app.get('/search', handleGetSearch);

//Start the app listening on port 8080
app.listen(8080);


function handleGetSearch(request, response){
    var urlObj = url.parse(request.url, true);

    //Extract object containing queries from URL object.
    var queries = urlObj.query;

    //Get the pagination properties if they have been set. Will be  undefined if not set.
    var numItems = queries['num_items'];
    var offset = queries['offset'];
    searchQuery = queries['q'];
    searchMode = true;

    getTotalCarsCount(response, numItems, offset);
}


var searchMode = false;
var searchQuery = "";

/* Handles GET requests sent to web service.
   Processes path and query string and calls appropriate functions to
   return the data. */
function handleGetRequest(request, response){
    //Parse the URL
    var urlObj = url.parse(request.url, true);

    //Extract object containing queries from URL object.
    var queries = urlObj.query;

    //Get the pagination properties if they have been set. Will be  undefined if not set.
    var numItems = queries['num_items'];
    var offset = queries['offset'];

    //Split the path of the request into its components
    var pathArray = urlObj.pathname.split("/");

    //Get the last part of the path
    var pathEnd = pathArray[pathArray.length - 1];

    //If path ends with 'cars' we return all cars
    if(pathEnd === 'cars'){
        getTotalCarsCount(response, numItems, offset);//This function calls the getAllCars function in its callback
        return;
    }

    //If path ends with cars/, we return all cars
    if (pathEnd === '' && pathArray[pathArray.length - 2] === 'cars'){
        getTotalCarsCount(response, numItems, offset);//This function calls the getAllCars function in its callback
        return;
    }

    //If the last part of the path is a valid user id, return data about that user
    var regEx = new RegExp('^[0-9]+$');//RegEx returns true if string is all digits.
    if(regEx.test(pathEnd)){
        getCar(response, pathEnd);
        return;
    }

    //The path is not recognized. Return an error message
    response.status(HTTP_STATUS.NOT_FOUND);
    response.send("{error: 'Path not recognized', url: " + request.url + "}");
}

function carSearch(response, totNumItems, numItems, offset){
    //Select the cars data using JOIN to convert foreign keys into useful data.
    var sql = "SELECT * FROM car WHERE description LIKE '%" + searchQuery + "%'";

    //Limit the number of results returned, if this has been specified in the query string
    if(numItems !== undefined && offset !== undefined && searchQuery ){
        sql += "LIMIT " + numItems + 'q' + " OFFSET " + offset;
    }

    //Execute the query
    connectionPool.query(sql, function (err, result) {

        //Check for errors
        if (err){
            //Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        //Create JavaScript object that combines total number of items with data
        var returnObj = {totNumItems: totNumItems};
        returnObj.cars = result; //Array of data from database


        //Return results in JSON format
        response.json(returnObj);
    });
}

/** Returns all of the cars, possibly with a limit on the total number of items returned and the offset (to
 *  enable pagination). This function should be called in the callback of getTotalCarsCount  */
function getAllCars(response, totNumItems, numItems, offset){
    //Select the cars data using JOIN to convert foreign keys into useful data.
    var sql = "SELECT car.id, car.image_url, car.description, car.features, car.price, car.url_id, url.domain, url.path, url.query_string " +
    "FROM ( (car INNER JOIN url ON car.url_id=url.id))";

    //Limit the number of results returned, if this has been specified in the query string
    if(numItems !== undefined && offset !== undefined ){
        sql += "ORDER BY car.id LIMIT " + numItems + " OFFSET " + offset;
    }

    //Execute the query
    connectionPool.query(sql, function (err, result) {

        //Check for errors
        if (err){
            //Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        //Create JavaScript object that combines total number of items with data
        var returnObj = {totNumItems: totNumItems};
        returnObj.cars = result; //Array of data from database

        //Return results in JSON format
        response.json(returnObj);
    });
}


/** When retrieving all cars we start by retrieving the total number of cars
    The database callback function will then call the function to get the car data
    with pagination */
function getTotalCarsCount(response, numItems, offset){
    var sql = "SELECT COUNT(*) FROM car";

    //Execute the query and call the anonymous callback function.
    connectionPool.query(sql, function (err, result) {

        //Check for errors
        if (err){
            //Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        //Get the total number of items from the result
        var totNumItems = result[0]['COUNT(*)'];

        //Call the function that retrieves all cars
        if(searchMode){
            carSearch(response, totNumItems, numItems, offset);
        }
        else
            getAllCars(response, totNumItems, numItems, offset);
    });
}


/** Returns the car with the specified ID */
function getCar(response, id){
    //Build SQL query to select car with specified id.
    var sql = "SELECT car.id, car.image_url, car.description, car.features, car.price, car.url_id, url.domain, url.path, url.query_string" +
        "FROM ( (car INNER JOIN url ON car.url_id=url.id) " +
        "WHERE car.id=" + id;

    //Execute the query
    connectionPool.query(sql, function (err, result) {

        //Check for errors
        if (err){
            //Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        //Output results in JSON format
        response.json(result);
    });
}

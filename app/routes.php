<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the Closure to execute when that URI is requested.
|
*/

Route::get('/', function()
{
	return View::make('index');
});

Route::get('/DBTEST', function() {
	DB::insert('insert into email_log (email, access) values (?, ?)', array("test1", "true"));
});

Route::post('/insertEmail', function() {
	//return Response::json(array("test" => "function"), 200);
	// $input = Input::json()->all();
	// return Response::json($input, 200);
	$newEmail = Input::get('email');
	if(empty(DB::select('select * from email_log where email = ?', array($newEmail)))) {
		// if(preg_match('^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$', $newEmail)) {
		if(1 == 1) {
			if(DB::insert('insert into email_log (email,access) values (?, ?)', array($newEmail, 't'))) {
				return Response::json(array('success' => 'Email has been entered'), 200);
			} else {
				return Response::json(array('error' => 'Insertion failed.'), 200);
			}
		} else {
			return Response::json(array('error' => 'Not a valid email address.'), 200);
		}
	} else {
		return Response::json(array('error' => 'Email already exists.'), 200);
	}
});

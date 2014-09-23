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

Route::post('/insertEmail', function($newEmail) {
	if(empty(DB::select('select * from email_log where email = ?', array($newEmail)))) {
		if(preg_match('^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,4})$', $newEmail)) {
			if(DB::insert('insert into email_log (email,access) values (?, ?)', array($newEmail, 't'))) {
				return Response::json(array('success' => 'Email has been entered'), 200);
			} else {
				return Response::json(array('error' => 'Insertion failed.'), 500);
			}
		} else {
			return Response::json(array('error' => 'Not a valid email address.'), 400);
		}
	} else {
		return Response::json(array('error' => 'Email already exists.'), 400);
	}
});

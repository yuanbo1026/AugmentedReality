<?php
require_once("config.inc.php");
$url = $_GET["url"];

$db = connectDb();

if(isset($url)){
  insertTupple($url, $db);
  $db = null;
  header("Location: " . $url);
}else{
  header("Status: 404 Not Found");
}

function insertTupple($url, $db) {
    $stmt = $db->prepare('
      INSERT INTO
        redirects
        (url)
      VALUES
        (
          Coalesce(:x)
        )
    ');
    $timestamp = time();

    $stmt->bindParam(':x', $url);

    $stmt->execute();
}

function connectDb() {
  try {
    $dbconn = 'mysql:host=' . DBHOST . ';dbname=' . DBDATA ; 
    $db = new PDO($dbconn , DBUSER , DBPASS , array(PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8'));
    $db->setAttribute(PDO::ATTR_ERRMODE , PDO::ERRMODE_EXCEPTION);
     return $db; 
  }
  catch(PDOException $e) {
    error_log('message:' . $e->getMessage());
  }
}

?>
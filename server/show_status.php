<?php
error_reporting(E_ALL);
ini_set('display_errors',1);
include('dbcon.php');

$status_index=$_GET['status_index'];

$stmt = $con->prepare("SELECT * FROM status_nutrition WHERE status_index = '$status_index'");
$stmt->execute();

if ($stmt->rowCount() > 0)
{
    $data = array();
    while($row=$stmt->fetch(PDO::FETCH_ASSOC))
    {
        extract($row);
        array_push($data,
                   array('first_dish'=>$first_dish, 'second_name'=>$second_name
        ));
    }
    header('Content-Type: application/json; charset=utf8');
    $json = json_encode(array("webnautes"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
    echo $json;
}
?>

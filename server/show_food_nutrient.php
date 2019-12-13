<?php
    error_reporting(E_ALL);
    ini_set('display_errors',1);
    include('dbcon.php');

    $food_name=$_GET['food_name'];

    $stmt = $con->prepare("SELECT * FROM nutrition WHERE food_name = '$food_name'");
    $stmt->execute();

    if ($stmt->rowCount() > 0)
    {
        $data = array();
        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
           extract($row);
        array_push($data,
            array('food_name'=>$food_name, 'calories'=>$calories, 'carbs'=>$carbs, 'proteins'=>$proteins, 'fats'=>$fats,
             'sugars'=>$sugars, 'sodium'=>$sodium, 'cholesterol'=>$cholesterol
        ));
        }
        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("webnautes"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
    }
?>

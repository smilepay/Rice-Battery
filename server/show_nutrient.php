<?php
    error_reporting(E_ALL);
    ini_set('display_errors',1);
    include('dbcon.php');

    $user_name=$_GET['user_name'];

    $stmt = $con->prepare("SELECT * FROM day_eat WHERE user_name = '$user_name'");
    $stmt->execute();

    if ($stmt->rowCount() > 0)
    {
        $data = array();
        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
           extract($row);
        array_push($data,
            array('user_name'=>$user_name,'today_date'=>$today_date, 'today_carbon'=>$today_carbon, 'today_protein'=>$today_protein, 'today_fat'=>$today_fat,
             'today_nat'=>$today_nat, 'today_sugar'=>$today_sugar, 'today_cole'=>$today_cole
        ));
        }
        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("webnautes"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
    }
?>

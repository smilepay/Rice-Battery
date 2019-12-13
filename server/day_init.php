<?php
    error_reporting(E_ALL);
    ini_set('display_errors',1);

    include('dbcon.php');
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
        $user_name=$_POST['user_name'];
       

        if(!isset($errMSG))
        {
            try{
                $stmt = $con->prepare('INSERT INTO day_eat(user_name,user_name, today_date,today_carbon, today_protein, today_fat,today_nat,today_sugar,today_cole)
                VALUES(:user_name,'0','0','0','0','0','0','0')');

                $stmt->bindParam(':user_name', $user_name);
             

                if($stmt->execute())
                {
                    $successMSG = "성공";
                }
                elsev
                {
                    $errMSG = "에러";
                }

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage());
            }
        }

    }

?>
<?php
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( !$android )
    {
?>
    <html>
       <body>

            <form action="<?php $_PHP_SELF ?>" method="POST">
                user_name: <input type = "text" name = "user_name" />
                user_age: <input type = "text" name = "user_age" />
                user_gender: <input type = "text" name = "user_gender" />
                user_height: <input type = "text" name = "user_height" />
                user_weight: <input type = "text" name = "user_weight" />

                user_active: <input type = "text" name = "user_active" />
                user_basic: <input type = "text" name = "user_basic" />
                user_digest: <input type = "text" name = "user_digest" />
                user_nat: <input type = "text" name = "user_nat" />
                user_sugar: <input type = "text" name = "user_sugar" />
                user_cole: <input type = "text" name = "user_cole" />
                <input type = "submit" name = "submit" />
            </form>
       </body>
    </html>

<?php
    }
?>
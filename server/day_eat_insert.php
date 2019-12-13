<?php
    error_reporting(E_ALL);
    ini_set('display_errors',1);

    include('dbcon.php');
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
        $user_name=$_POST['user_name'];
        $today_carbon =$_POST['today_carbon '];
        $today_protein=$_POST['today_protein'];
        $today_fat=$_POST['today_fat'];
        $today_nat=$_POST['today_nat'];	
	$today_sugar=$_POST['today_sugar'];
	$today_cole=$_POST['today_cole'];

        if(!isset($errMSG))
        {
            try{
                $stmt = $con->prepare('UPDATE day_eat SET today_carbon ='$today_carbon',today_protein='$today_protein',today_fat='$today_fat',today_nat='$today_nat',today_sugar='$today_sugar',today_cole='$today_cole' where user_name='$user_name');

                $stmt->bindParam(':user_name', $user_name);
                $stmt->bindParam(':today_carbon', $today_carbon);
                $stmt->bindParam(':today_protein', $today_protein);
                $stmt->bindParam(':today_fat', $today_fat);
                $stmt->bindParam(':today_nat', $today_nat);
		$stmt->bindParam(':today_sugar', $today_sugar);
		$stmt->bindParam(':today_cole', $today_cole);

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
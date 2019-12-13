<?php
    error_reporting(E_ALL);
    ini_set('display_errors',1);

    include('dbcon.php');
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
        $user_name=$_POST['user_name'];
        $user_age=$_POST['user_age'];
        $user_gender=$_POST['user_gender'];
        $user_height=$_POST['user_height'];
        $user_weight=$_POST['user_weight'];	
	$user_active=$_POST['user_active'];
	$user_basic=$_POST['user_basic'];
	$user_work=$_POST['user_work'];
	$user_digest=$_POST['user_digest'];
	$user_whole=$_POST['user_whole'];
	$user_nat=$_POST['user_nat'];
	$user_sugar=$_POST['user_sugar'];
	$user_cole=$_POST['user_cole'];

        if(!isset($errMSG))
        {
            try{
                $stmt = $con->prepare('INSERT INTO user_information(user_name, user_age, user_gender, user_height, user_weight,user_active,user_basic,user_work,user_digest,user_whole,user_nat,user_sugar,user_cole)
                VALUES(:user_name, :user_age, :user_gender, :user_height, :user_weight, :user_active,:user_basic,:user_work,:user_digest,:user_whole,:user_nat,:user_sugar,:user_cole)');

                $stmt->bindParam(':user_name', $user_name);
                $stmt->bindParam(':user_age', $user_age);
                $stmt->bindParam(':user_gender', $user_gender);
                $stmt->bindParam(':user_height', $user_height);
                $stmt->bindParam(':user_weight', $user_weight);
		$stmt->bindParam(':user_active', $user_active);
		$stmt->bindParam(':user_basic', $user_basic);
		$stmt->bindParam(':user_work', $user_work);
		$stmt->bindParam(':user_digest', $user_digest);
		$stmt->bindParam(':user_whole', $user_whole);
		$stmt->bindParam(':user_nat', $user_nat);
		$stmt->bindParam(':user_sugar', $user_sugar);
		$stmt->bindParam(':user_cole', $user_cole);

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
<?php
$target_path  = "upload/";
$target_path = $target_path . basename( $_FILES['uploadedfile']['name']);
$userid = $_POST['userid'];
$title = $_POST['title'];
$userid=mysql_real_escape_string($userid);
$title=mysql_real_escape_string($title);
$link = mysql_connect('localhost:3306', 'root', '');
if (!$link) {
    die('Could not connect: ' . mysql_error());
}
mysql_query("SET SESSION time_zone = '+22:00'"); 
mysql_select_db("imagesharingdatabase", $link);
$result = mysql_query("SELECT * FROM image_details", $link);
$num_el = mysql_num_rows($result);
$num_el+=1;
$img_name="upload/img".$num_el.".jpg";
echo 'Connected successfully';


if(move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $target_path)) {
 echo "The file ".  basename( $_FILES['uploadedfile']['name']).
 " has been uploaded";
rename($target_path,$img_name);
$img_path=mysql_real_escape_string($img_name);
$result=mysql_query("SELECT * from user_details WHERE username='$userid'");
$result=mysql_num_rows($result);
if($result==0)
{
	$result=mysql_query("INSERT INTO user_details VALUES('$userid',100,0,'".date("Y-m-d H:i:s", time())."','','')");
}
$result=mysql_query("UPDATE user_details SET no_uploads=no_uploads+1 WHERE username='$userid'");
$result=mysql_query("UPDATE user_details SET points=points-5 WHERE username='$userid'");
$result=mysql_query("INSERT INTO image_details VALUES($num_el,'$title','$userid','".date("Y-m-d H:i:s", time())."','$img_path',0,0)");

 } else{
 echo "There was an error uploading the file, please try again!";
}
mysql_close($link);
?>

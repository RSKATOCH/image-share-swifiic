<?php
$target_dir = "upload/";
$target_dir = $target_dir . basename( $_FILES['uploadedfile']['name']);
$uploadOk=1;

if (!($uploadFile_type == "image/gif")) {
    echo "Sorry, file is not allowed.";
    $uploadOk = 0;
}
else if (!($uploadFile_type == "image/png")) {
    echo "Sorry, file is not allowed.";
    $uploadOk = 0;
}
else if (!($uploadFile_type == "image/jpeg")) {
    echo "Sorry, file is not allowed.";
    $uploadOk = 0;
}
else if (!($uploadFile_type == "image/jpg")) {
    echo "Sorry, file is not allowed.";
    $uploadOk = 0;
} 

$link = mysql_connect('192.168.137.1:3306', 'user', 'pswrd');
if (!$link) {
    die('Could not connect: ' . mysql_error());
}
echo 'Connected successfully';
mysql_close($link);

// we connect to localhost at port 3307
//$link = mysql_connect('127.0.0.1:3306', 'user', 'pswrd');
//if (!$link) {
//    die('Could not connect: ' . mysql_error());
//}
//echo 'Connected successfully';
//mysql_close($link);


// Check if $uploadOk is set to 0 by an error
if ($uploadOk==0) {
    echo "Sorry, your file was not uploaded.";
// if everything is ok, try to upload file
} else {
	$test=move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $target_dir);
    if ($test) {
        echo "The file ". basename( $_FILES['uploadedfile']['name']). " has been uploaded.";
	
	} else {
        echo "Sorry, there was an error uploading your file.";
    }
}
?> 
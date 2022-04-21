<?php
$username = "s2381410";
$password = "s2381410";
$database = "d2381410";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);
$output=array();

if ($result = mysqli_query($link, "SELECT * FROM LECTURER")) {
while ($row=$result->fetch_assoc()){
$output[]=$row;
}
}
mysqli_close($link);
echo json_encode($output);
?>

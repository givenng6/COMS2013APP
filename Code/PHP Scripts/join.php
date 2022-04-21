<?php
$username = "s2381410";
$password = "s2381410";
$database = "d2381410";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$name = $_REQUEST["STUDENT2"];
$group = $_REQUEST["GROUP"];

$output=array();

if ($r = mysqli_query($link, "UPDATE GROUPS SET STUDENT2 = '$name' WHERE GROUP_NAME = '$group' ")) {
while ($row=$r->fetch_assoc()){
$output[]=$row;
}
}
mysqli_close($link);
echo json_encode($output);
?>

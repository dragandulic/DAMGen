<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha/js/bootstrap.min.js"></script>
</head>
<body>
  <h1>${className} page</h1>
<style> 
body {
  background-image: url('background.png'); 
  background-repeat: no-repeat;
}
table{
 margin: auto;
 width: 50% !important; 
 background: #F2F2E4 !important;
 color: black !important;
}
h1{ 
    text-align:center;
   }

</style>

<table class = "table table-bordered table-dark" id= ${className}>

    
</table>

</body>

<script type="text/javascript">

$(document).ready(function() {


      
      console.log("stigao");
            $.ajax({
                type: 'GET',
                url: 'http://localhost:8080/generated/${className?lower_case}',
                contentType: 'application/json; charset=utf-8',
                success: function (response) {
                    var myObj = JSON.stringify(response);
                    //populate th-s
                    console.log(response[1]);
                    var table = document.getElementById("${className}");
                    var thead = document.createElement("thead");
                    table.appendChild(thead);
                    var tr = document.createElement("tr");
                     thead.appendChild(tr);
                    for (let key in response[0]) {
                     var th = document.createElement("th");
                     th.innerText = key;
                     tr.appendChild(th);
                   
                      }
                      
                      //populate tr-s
                    for(i=0;i<response.length;i++)
                    {
                    var trow = document.createElement("tr");
                    table.appendChild(trow);
                    for (let key in response[i]) {
                     var td = document.createElement("td");
                     td.innerText = response[i][key];
                     trow.appendChild(td);
                   
                      }
                    }
                   
              },
                error: function () {
                 alert("error");
        }
            });
});
</script>
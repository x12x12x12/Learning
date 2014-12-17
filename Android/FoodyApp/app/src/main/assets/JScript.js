var ResIndex=-1;
var Count=0;
function jsRes(iIndex)
{
    if(0 <= ResIndex)
    {
        if(iIndex == ResIndex){
            if(0 == Count){
                Count=1;
                document.getElementById("sdiv"+ResIndex).style.display = "none";
            }
            else{
                Count=0;
                document.getElementById("sdiv"+ResIndex).style.display = "block";
            }
        }
        else{document.getElementById("sdiv"+ResIndex).style.display = "none";}

    }
    if( iIndex != ResIndex){
        var sdiv=document.getElementById("sdiv"+iIndex);
        if(sdiv.innerHTML == ""){
            var HTML = pRes.ShowChildItem(iIndex);
            sdiv.innerHTML=HTML;
        }
        sdiv.style.display = "block";
        ResIndex=iIndex;
    }
}

function SelectContext(iIndex,iCSel,bcontext){
    if(0 == bcontext){
        pFood.ShowChildActivity(iIndex,0,0);
    }
    else{
        if(1 == bcontext){
        pRes.ShowChildActivity(ResIndex,iIndex, 1);}
        else{pOrd.ShowChildActivity(iIndex,0, 2);}
    }
}

function OrderEdit(iIndex){
    var sspan=document.getElementById("SpanEdit"+iIndex);sspan.style.display = "inline";
}

function setBlur(iIndex){
    var text=document.getElementById("text"+iIndex);
    if(text.value != "" && text.value != " "){
    var New=pOrd.CalMoney(iIndex,text.value);
    var Temp=document.getElementById("aQuantity"+iIndex);Temp.innerHTML="Quantity= "+text.value;
    var Old=document.getElementById("aMoney"+iIndex).innerHTML;
    document.getElementById("aMoney"+iIndex).innerHTML=New;
    text.value="";
    var total=document.getElementById("TotalMoney");
    //var tm=pOrd.CalTotal(parseFloat(Old),parseFloat(New),parseFloat(total.innerHTML));
    total.innerHTML=((parseFloat(total.innerHTML)-parseFloat(Old))+parseFloat(New));
    }
    document.getElementById("SpanEdit"+iIndex).style.display="none";
}

function OrderDelete(iIndex){
    pOrd.OrderDelete(iIndex);
    //var total=document.getElementById("TotalMoney");
    //total.innerHTML=parseFloat(total.innerHTML)-parseFloat(document.getElementById("aMoney"+iIndex).innerHTML);

    //var sdiv=document.getElementById("DParent"+iIndex);sdiv.style.display = "none";
}

function SetInfo(index){
    var Info;//=document.getElementById(""+index).value;
    if(0==index){
            Info=document.getElementById("0").value;
        }else{
            if(1==index){
                Info=document.getElementById("1").value;
            }else{
                Info=document.getElementById("2").value;
            }
        }
    //alert(Info);
    pOrd.SetInfo(index,Info);
}

function SendOrder(){
    pOrd.SendOrder(document.getElementById("TotalMoney").innerHTML);
}

function jSendToServer(sJson){
        //var URL="http://localhost:8080/rest-webapp/rest/item/1";
        pOrd.Mess("Ready to send server");
        $('#username').
        $.ajax({
                            type:"Get",
                            url:"http://www.w3schools.com/ajax/tryit.asp?filename=tryajax_first",
                            //data:sJson,
                            success: function (data) {
                                //$('#Role').val(data);
                                //$('p').text(data);
                                pOrd.ResultBill(data);
                                pOrd.Mess(data);
                            }
        });
}

function ReadySend(){
    var UName=document.getElementById("username").value;
    var Phone=document.getElementById("phone").value;
    var Addr=document.getElementById("addr").value;
    if(""==UName||" "==UName||""==Phone||" "==Phone||""==Addr||" "==Addr){
        pOrd.Mess("One of information's field is null :(, try again");
    }else{
    pOrd.SendToServer(UName,Phone,Addr);}
}
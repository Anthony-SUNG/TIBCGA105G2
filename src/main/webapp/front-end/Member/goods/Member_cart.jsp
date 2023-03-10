<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ page import="com.goods.model.Goods.pojo.*" %>
<%@ page import="com.goods.model.service.*" %>
<%@ page import="com.goods.model.Cart.pojo.Cart" %>
<%@ page import="com.store.model.service.StoreService" %>
<%@ page import="com.goods.model.Cart.pojo.CartItem" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--่ณผ็ฉ่ป--%>


<jsp:useBean id="goodsSvc" scope="page" class="com.goods.model.service.GoodsService"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>๐่ณผ็ฉ่ป</title>
</head>

<body>
<!-- header start -->
<%@ include file="/front-end/Member/01h/headerin.jsp" %>
<!-- header end -->
<!-- main -->
<div class="container-fluid container">
    <div class="row">
        <main role="main" class="col-md-9 m-sm-auto col-lg-10 px-md-4 my-5 container">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom text-center">
                <h1 class="h2 text-center mx-auto mt-5">๐่ณผ็ฉ่ป</h1>
            </div>
            <section class="section container pt-0" id="contacts">
                <!-- ๅๅๅ่กจ้ๅง -->
                <form class="pt80 pb80 col-md-9 m-sm-auto col-lg-10 px-md-4 my-5 container" method="post"
                      action="<%=request.getContextPath()%>/order/order.do" name="form1">
                    <input type="hidden" name="action" value="checkout">
                    <div class="container px-4 px-lg-5 mt-5 ">
                        <div class="row">
                            <div class="col-sm-12 mb-5">
                                <div class="table-responsive-sm" id="listTable">
                                    <table class="table table-hover">
                                        <thead class="all-text-white bg-grad">
                                        <tr>
                                            <th scope="col" class="text-center">ๅๅๅ็จฑ</th>
                                            <th scope="col">ๅๅ็ง็</th>
                                            <th scope="col">ๅฎๅน</th>
                                            <th scope="col">ๆธ้</th>
                                            <th scope="col">ๅฐ่จ</th>
                                            <th scope="col"></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        </tbody>
                                        <tfoot>
                                        </tfoot>
                                    </table>
                                </div>
                                <hr>
                                <div class="d-flex justify-content-between col-12">
                                    <button type="submit" class="btn btn-secondary btn-lg  mt-auto fs-4 checkout col-12">ๅๅพ็ต่ณฌ</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </section>
        </main>
        <!-- ๅๅๅ่กจ็ตๆ -->
    </div>
</div>

<!-- main -->

<!-- footer start -->
<%@ include file="/front-end/Member/01h/footerin.jsp" %>
<!-- footer end -->
<script>
    $("a:contains(๐)").closest("a").addClass("active disabled topage");
</script>
<script>
    // ----------------------------------------------------
    (async function getCart() {
        let url = "<%=request.getContextPath()%>/cart/Write?memId=<%= request.getSession().getAttribute("memId")%>";//
        let response = await fetch(url, {method: 'post'});
        let carts = await response.json();
        listCarts(carts);
    })();

    //ๅๆ็ๆ่กจๆ?ผ
    function listCarts(carts) {
        //็งป้คๅๆฌๅญๅจ็่กจๆ?ผ
        $("#listTable").empty();

        //็ๆ่กจๆ?ผ
        let table = document.createElement("table");

        //็ๆ่กจ้?ญ
        table.innerHTML += "<thead><tr><th>ๅๅบๅ็จฑ</th><th>ๅๅๅ็จฑ</th>"
            + "<th>ๅๅๅนๆ?ผ</th><th>ๅๅๆธ้</th><th>็ธฝ้้ก</th><th>ๅช้ค</th>"
            + "</tr></thead>";

        //็ๆ่กจๆ?ผๅงๅฎน
        let tbody = document.createElement("tbody");

//     console.log(carts);
        //ๅพช็ฐไพๅ?ๅฅๅๅๆธ้็ๆ่กจๆ?ผๆธ้
        for (let storeId in carts.storeMap) {
            for (let goodsId in carts.storeMap[storeId]) {
                console.log(carts.storeMap[storeId][goodsId].goodsName);
                let tr = document.createElement("tr");
                let storeId2 = carts.storeMap[storeId][goodsId].storeId;
                let goodsId2 = carts.storeMap[storeId][goodsId].goodsId;
                let storeName = carts.storeMap[storeId][goodsId].storeName;
                let goodsName = carts.storeMap[storeId][goodsId].goodsName;
                let goodsPrice = carts.storeMap[storeId][goodsId].goodsPrice;
                let detailQuantity = carts.storeMap[storeId][goodsId].detailQuantity;
                let goodsTotalPrice = carts.storeMap[storeId][goodsId].goodsTotalPrice;

                tr.insertAdjacentHTML("beforeend", '<td storeId= ' + storeId2 + ' goodsId=' + goodsId2 + '>' + storeName + '</td>');
                tr.insertAdjacentHTML("beforeend", '<td storeId= ' + storeId2 + ' goodsId=' + goodsId2 + '>' + goodsName + '</td>');
                tr.insertAdjacentHTML("beforeend", '<td storeId= ' + storeId2 + ' goodsId=' + goodsId2 + '>' + "$" + goodsPrice + '</td>');
// //             --------------------------------------------------------------
// //             --------------------------------------------------------------
                tr.insertAdjacentHTML("beforeend", '<td storeId= ' + storeId2 + ' goodsId=' + goodsId2 + '>' + detailQuantity + '</td>');
                tr.insertAdjacentHTML("beforeend", '<td storeId= ' + storeId2 + ' goodsId=' + goodsId2 + '>' + "$" + goodsTotalPrice + '</td>');
                tr.insertAdjacentHTML("beforeend", '<input type="hidden" name=memId value=' + '<%=request.getSession().getAttribute("memId")%>' + '>');
                tr.insertAdjacentHTML("beforeend", '<input type="hidden" name=storeId value=' + storeId2 + '>');
                tr.insertAdjacentHTML("beforeend", '<input type="hidden" name=goodsId value=' + goodsId + '>');
                tr.insertAdjacentHTML("beforeend", '<input type="hidden" name=goodsPrice value=' + goodsPrice + '>');
                tr.insertAdjacentHTML("beforeend", '<input type="hidden" name=detailQuantity value=' + detailQuantity + '>');
                tr.insertAdjacentHTML("beforeend", '<input type="hidden" name=goodsName value=' + goodsName + '>');
                tr.insertAdjacentHTML("beforeend", '<td><input type="button" value="x" storeId=' + storeId + ' goodsId=' + goodsId + ' onclick="removeCartList(this)"/></td>');
                tbody.append(tr);
            }
            table.append(tbody);
            table.setAttribute("class", "tableclass table table-hover");
            table.setAttribute("id", "tableRows");
            $("#listTable").append(table);
        }

    }

    async function removeCartList(btn) {
        let tr = btn.parentNode.parentNode;
        let storeId = btn.getAttribute("storeId");
        let goodsId = btn.getAttribute("goodsId");
        console.log(goodsId);
        let url = "<%=request.getContextPath()%>/cart/delete?goodsId=" + goodsId + "&storeId=" + storeId;
        console.log(url);
        let response = await fetch(url, {method: 'post'});
        tr.remove();
    }

    async function removeCheckList(removeCheck) {
        let tr = removeCheck.parentNode.parentNode;
        let info = tr.children;
        let children = tr.children;
        let price = parseInt(children[4].innerHTML);
        all -= price;
        changeTotal();					//ๆน่ฎๅ่จ้้ก
        let storeId = btn.getAttribute("storeId");
        let goodsId = btn.getAttribute("goodsId");
        let url = "<%=request.getContextPath()%>/cart/delete?goodsId=" + goodsId + "&storeId=" + storeId;
        let response = await fetch(url, {method: 'post'});
        tr.remove();

    }

    async function reduce(btn) {			//ๆธๅฐๅๅๆธ้
        let amount = btn.nextElementSibling.value;
        if (amount == 0) {
            return;				//่ฅๅๅ็ญๆผ0ๅ้ๅบ
        }
        amount--;
        btn.nextElementSibling.value = amount;		//ๆดๆฐๅๅๆธ้

        let value = parseInt(btn.parentNode.previousElementSibling.innerHTML);	//็ฒๅๅๅๅฎๅน
        btn.parentNode.nextElementSibling.innerHTML = value * amount;		//ๆดๆฐๅๅ็ธฝๅน
        all -= value;		//ๆดๆฐ็ธฝๅนใ
        changeTotal();		//ๅทๆฐ็ธฝๅน

        let storeId = btn.getAttribute("storeId");
        let goodsId = btn.getAttribute("goodsId");
        let path = window.location.pathname;
        let webCtx = path.substring(0, path.indexOf('/', 1));
        let url = webCtx + `/cart/reduce?storeId=${storeId}&goodsId=${goodsId}`;
        let response = await fetch(url, {method: 'post'});

    }

    async function increase(btn) {		//ๅขๅ?ๅๅๆธ้
        let amount = btn.previousElementSibling.value;
        amount++;
        btn.previousElementSibling.value = amount;		//ๆดๆฐๅๅๆธ้

        let value = parseInt(btn.parentNode.previousElementSibling.innerHTML);
        btn.parentNode.nextElementSibling.innerHTML = value * amount;

        all += value;		//ๆดๆฐ็ธฝๅน
        changeTotal();		//ๅทๆฐ็ธฝๅน

        let storeId = btn.getAttribute("storeId");
        let goodsId = btn.getAttribute("goodsId");
        let path = window.location.pathname;
        let webCtx = path.substring(0, path.indexOf('/', 1));
        let url = webCtx + `/cart/add?storeId=${storeId}&goodsId=${goodsId}`;
        let response = await fetch(url, {method: 'post'});
    }

    function changeTotal() {		//ๆดๆฐtotalๅฝๆธ๏ผๅจๆฏๆฌกๆน่ฎ็ตๅธณๆธๅฎๆไฝฟ็จใ
        let total = document.getElementById("total");
        total.innerHTML = all;
    }

    window.onload = function () {
        let checkout = document.getElementById("checkOut");
        checkout.addEventListener("click", swalCheck)
// +++++++++++++++++++++++++++++++++++++++++
//     +++++++++++++++++++++++++++++++++++++++++++++++++
        function swalCheck() {
            // swal({
            //     title: "ๅณๅฐ้ฒ่กๆฃๆฌพ",
            //     text:`ๅณๅฐๆฃๆฌพ${all}้ปๆธ`,
            //     icon: "warning",
            // })
            swal({
                title: "่ซ็ขบ่ช",
                text: `ๅณๅฐๆฃๆฌพ${all}้ปๆธ`,
                icon: "warning",
                buttons: true,
                dangerMode: true,
            })
                .then((willDelete) => {
                    if (willDelete) {
                        swal("ๆจๅทฒๆๅๆฃๆฌพ", {
                            icon: "success",
                            buttons: false,
                        });
                        setTimeout(() => {
                            cleanRedis();
                        }, 1000)

                    } else {
                        swal("ๆจๅทฒๅๆถๆฃๆฌพ");
                    }
                });
        }
    }


</script>


</body>

</html>
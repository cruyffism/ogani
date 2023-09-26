$(document).ready(function () {
    productListAjax(1, "product_name", "desc", 1);
})

function productListAjax(page, sortColumn, sortType, idx) {
    const innerHtml = $("#productList")
    const f = document.getElementById("form1");
    f.page.value = page;
    f.sortColumn.value = sortColumn;
    f.sortType.value = sortType;
    f.idx.value = idx;
    f.searchText.value = $('#searchText').val();
    // id가 searchtext인 애의 값(list.html의 39라인)을 list.html의 31라인의 value="" 에다가 값을 넣어주는 것.
    const select_value = document.getElementById('searchType2');
    //teamList.html 에서 id가 searchType인 애의 값을 가져와서 변수 select_value에 넣는다.
    f.searchType.value = select_value.options[select_value.selectedIndex].value;
    // select_value 변수에서 option이 선택된 값을 list.htmldml 32라인의 value=""에 넣는거!
    $.ajax({
        url: "/admin/productListAjax",
        type: 'GET',
        cache: false,
        data: $('#form1').serialize(),
        dataType: "html",
        async: false,
        success: function (data) {
            $(innerHtml).html(data) // innerHtml에 html(data)를 뿌려라
            if (sortType !== 'undefined') {
                if (sortType === 'DESC') {
                    $(`#sort`+idx).attr({src:"../../static/img/admin/삼각형아래.png"})
                } else {
                    $(`#sort`+idx).attr({src:"../../static/img/admin/삼각형위.png"})
                }
            }
            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })
}

function productSortUpDown(idx) {
    const imgTag = document.getElementById("sort" + idx);
    if ($('#sortType').val() === 'DESC' && imgTag.className === $('#sortColumn').val()) {
        productListAjax('1', imgTag.className, 'ASC', idx);
    } else {
        productListAjax('1', imgTag.className, 'DESC', idx);
    }
}

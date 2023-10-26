function productRegisterForm(){
    const f = document.getElementById("productRegisterForm");

    // id가 searchtext인 애의 값(list.html의 39라인)을 list.html의 31라인의 value="" 에다가 값을 넣어주는 것.
    const select_value = document.getElementById('productClassification2');
    //teamList.html 에서 id가 searchType인 애의 값을 가져와서 변수 select_value에 넣는다.
    f.productClassification.value = select_value.options[select_value.selectedIndex].value;
    // select_value 변수에서 option이 선택된 값을 list.htmldml 32라인의 value=""에 넣는거!
    f.submit();
}
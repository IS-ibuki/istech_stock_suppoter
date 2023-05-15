
document.addEventListener('DOMContentLoaded', function(){
	
	//サイドバーからのページ遷移を処理する関数
	function movePage(){
		const item = this;
		switch(item.id){
			case 'product-index':
				window.location.href = "/products";
				brack;
			case 'product-add':
				window.location.href = "/products/add";
				brack;
			case 'product-bulkinsert':
				window.location.href = "/products/add/bulk";
				brack;
			case 'composition-bulkinsert':
				window.location.href = "/compositions/add/bulk";
				brack;
			case 'product-shipping':
				window.location.href = "/products/ship/choice";
				brack;
			case 'order-index':
				window.location.href = "/orders";
				brack;
			case 'order-add':
				window.location.href = "/orders/add";
				brack;
			case 'client-index':
				window.location.href = "/clients";
				brack;
			case 'client-add':
				window.location.href = "/clients/add";
				brack;
		}
	}
	
	function displayContentChild(){
		//class = content-groupの要素を取得
		const childContent= this.nextElementSibling;
		childContent.classList.toggle("is-open");
		const childCount = childContent.childElementCount;
		const childHeight = childContent.firstElementChild.offsetHeight;
		//アコーディオンメニューの高さを決定
		if(childContent.classList.contains("is-open")){
			childContent.style.height = childCount * childHeight + 'px';
		}else{
			childContent.style.height = '0px';
		}
				
		const els = this.lastElementChild.lastElementChild;
		els.classList.toggle("is-open");
	}
	
	const sideberOpenBtn = document.querySelectorAll(".content-wrapper");
	sideberOpenBtn.forEach(element => {
		element.addEventListener("click", displayContentChild);
	});
	
	const sideberContentItem = document.querySelectorAll(".content-group-item");
	sideberContentItem.forEach(element => {
		element.addEventListener("click", movePage);
	});
	
	document.querySelector("#bbb").checked = true;;
	
	
	const ss=sessionStorage;
	for(let i=0;i<ss.length;i++){
	    const n=ss.key(i);
	    const v=ss.getItem(ss.key(i));
	    console.log([n,v]);
	   document.querySelector(`[name="${n}"][value="${v}"]`).checked=true;
 	}
	
});



//サイドバー表示切替
function toggleSideber(){
	const sideberBtn = document.getElementById("sideber-id");
	sideberBtn.classList.toggle("sideber-active");

}


//メニュー要素の表示切り替え
function toggleMenu(){
	const toggle = document.getElementById("popup-toggle");
	
	if(toggle.classList.contains("active-flex")){
		//要素非表示
		toggle.classList.replace("active-flex","deactive")
	}else{
		//要素表示
		toggle.classList.replace("deactive","active-flex")
	}
	
}

let pushCnt = 0;
function addCompositionRowForm(){
	console.log(serialNum);
	
	if(pushCnt == 20){
		alert('一括登録は最大20件までです。');
		return;
	}
	pushCnt++;
	
	const form = document.getElementById("addCompositionForm");
	form.style.display = 'block';			
	
	var rowNum = document.getElementsByClassName("formRow").length;
	
	//親製品ID
	var parentId = document.createElement("input");
	parentId.setAttribute("type", "hidden");
	parentId.setAttribute("id", "compositionFormList" + rowNum + ".parent.serialNum");
	parentId.setAttribute("class", "compositionFormList");
	parentId.setAttribute("name", "compositionFormList[" + rowNum + "].parent.serialNum");
	parentId.setAttribute("value", serialNum);
	
	//子品番
	var childId = document.createElement("input");
	childId.setAttribute("type", "text");
	childId.setAttribute("id", "compositionFormList" + rowNum + ".child.serialNum");
	childId.setAttribute("class", "compositionFormList");
	childId.setAttribute("name", "compositionFormList[" + rowNum + "].child.serialNum");
	childId.setAttribute("list", "key");
    childId.setAttribute("value", "");
    
    var dataList = document.createElement("datalist");
    var option = document.createElement("option");
	var tdChildId = document.createElement("td");
	tdChildId.appendChild(childId);
	tdChildId.appendChild(dataList);
	
	//子入数
	var quantity = document.createElement("input");
	quantity.setAttribute("type", "number");
	quantity.setAttribute("id", "compositionFormList" + rowNum + ".quantity");
	quantity.setAttribute("class", "compositionFormList");
	quantity.setAttribute("name", "compositionFormList[" + rowNum + "].quantity")
    quantity.setAttribute("value", "");
	var tdQuantity = document.createElement("td");
	tdQuantity.appendChild(quantity);
	
	var deleteBtn = document.createElement("button");
	deleteBtn.setAttribute("type", "button");
	deleteBtn.setAttribute("onClick", "deleteCompositionRowForm(this)");
	deleteBtn.innerText = "削除";
	var tdDeleteBtn = document.createElement("td");
	tdDeleteBtn.appendChild(deleteBtn);
	
	var trEle = document.createElement("tr");
	trEle.setAttribute("class", "formRow");
	trEle.setAttribute("id", "row" + rowNum);
	trEle.appendChild(parentId);
	trEle.appendChild(tdChildId);
	trEle.appendChild(tdQuantity);
	trEle.appendChild(tdDeleteBtn);
	document.getElementById("formRows").appendChild(trEle);
	
}


function deleteCompositionRowForm(button){
	// 削除ボタンが押下された行を削除
	var target = button.parentNode.parentNode;
	target.remove();
	
	var rowForms = document.querySelectorAll(".formRow");
	
	if(rowForms.length == 0){
		document.getElementById("addCompositionForm").style.display = 'none';
	}
	
	
	for(let n = 0; n < rowForms.length; n++){
		
		
		rowForms.item(n).setAttribute("id", "row" + n);
		var row = document.querySelector("#row" + n).querySelectorAll(".compositionFormList");
		
		
		for(let i = 0; i < row.length ; i++){
			
			
			if(i == 0){
				row.item(i).setAttribute("id","compositionFormList" + n + ".parent.productId");
				row.item(i).setAttribute("name","compositionFormList[" + n + "].parent.productId");
			}else if(i == 1){
				row.item(i).setAttribute("id","compositionFormList" + n + ".child.serialNum");
				row.item(i).setAttribute("name","compositionFormList[" + n + "].child.serialNum");
			}else{
				row.item(i).setAttribute("id","compositionFormList" + n + ".quantity");				
				row.item(i).setAttribute("name","compositionFormList[" + n + "].quantity");				
			}
			
		}

	}
	
	
}

function deleteProductRowForm(button){
	// 削除ボタンが押下された行を削除
	var target = button.parentNode.parentNode;
	target.remove();
	
	var rowForms = document.querySelectorAll(".formRow");
	
	for(let n = 0; n < rowForms.length; n++){
		
		
		rowForms.item(n).setAttribute("id", "row" + n);
		var row = document.querySelector("#row" + n).querySelectorAll(".productList");
		
		
		for(let i = 0; i < row.length ; i++){
			
			
			if(i == 0){
				row.item(i).setAttribute("id","productList" + n + ".serialNum");
				row.item(i).setAttribute("name","productList[" + n + "].serialNum");
			}else if(i == 1){
				row.item(i).setAttribute("id","productList" + n + ".productName");
				row.item(i).setAttribute("name","productList[" + n + "].productName");
			}else if(i == 2){
				row.item(i).setAttribute("id","productList" + n + ".price");				
				row.item(i).setAttribute("name","productList[" + n + "].price");				
			}else if(i == 3){
				row.item(i).setAttribute("id","productList" + n + ".currentNum");				
				row.item(i).setAttribute("name","productList[" + n + "].currentNum");				
			}else {
				row.item(i).setAttribute("id","productList" + n + ".clientForm.clientNum");				
				row.item(i).setAttribute("name","productList[" + n + "].clientForm.clientNum");				
			}
			
		}

	}
	
	
}

function messagesDeactive(button){
	var notice = button.parentNode; 
	console.log(notice);
	notice.remove();
}


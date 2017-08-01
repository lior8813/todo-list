var index;
getIndex();

//Make a GET call to Retrieve DB existing records
function getRetrieve(){
    var xhr = new XMLHttpRequest();
    xhr.open("GET", '/retrieveRecords', true);
    xhr.onreadystatechange= function() {
    if (this.readyState!==4) return; // not ready yet
        if (this.status===200) { // HTTP 200 OK
            retrieve(this.responseText);
        } else {
            alert("not working!");
        }
    };
    xhr.send(null);
}
//getRetrieve();

//This adds new li element representing a task
function listItem(name, taskId, isDone){
    this.name = name;
    this.id = taskId;
    this.done = isDone;
    this.getHtml = function(){
        var li = document.createElement("li");
        var t = document.createTextNode(this.name);
        li.appendChild(t);
        var span = document.createElement("SPAN");
        var txt = document.createTextNode("\u00D7");
        span.className = "close";
        span.appendChild(txt);
        span.onclick = clickOnDelete;
        li.appendChild(span);
        var cb = document.createElement( "input" );
        cb.type = "checkbox";
        cb.onclick	= toggleChecked;
        if (isDone) {
            cb.checked = true;
            li.classList.toggle('checked');
        }
        else cb.checked = false;
        cb.id = taskId;
        li.appendChild(cb);
        document.getElementById("myUL").appendChild(li);
    }
}

//Gets array of Json strings and builds an new list item of it
function retrieve(jsonArr){
jsonArr = JSON.parse(jsonArr);
    for (j = 0; j < jsonArr.length; j++){
      new listItem(jsonArr[j].name,jsonArr[j].id,jsonArr[j].done).getHtml();
    }
}

function getIndex(){
    var xhr = new XMLHttpRequest();
    xhr.open("GET", '/getIndex', true);
    xhr.onreadystatechange= function() {
    if (this.readyState!==4) return; // not ready yet
        if (this.status===200) { // HTTP 200 OK
            index =this.response;
            getRetrieve();
        } else {
            alert("not working!");
        }
    };
    xhr.send(null);
}
function clickOnDelete() {
        var div = this.parentElement;
        div.style.display = "none";
        var request = new XMLHttpRequest();
        request.open('POST', '/removeElement', true);
        request.setRequestHeader("Content-type", "application/json");
        request.send('{ "data" : "' + div.childNodes[2].id + '" }'); //send the request
//        location.reload();
//        getRetrieve();
}
// Edits existing mission when clicking on a mission
var list = document.querySelector('ul');
list.addEventListener('click', function(ev) {
  if (ev.target.tagName === 'LI') {
    var inputValue = prompt("Do you wish to edit your mission name?", ev.target.childNodes[0].data);
    if (inputValue != '' && inputValue != null){
	    ev.target.childNodes[0].data = inputValue;}
	    var index = ev.target.childNodes[2].id ;
	    var request = new XMLHttpRequest();
              request.open('POST', '/updateTask', true);
              request.setRequestHeader("Content-type", "application/json");
              request.send('{ "name" : "' + inputValue + '" },{ "index" : "' + index + '" }'); //send the request
  }
}, false);

function changeTaskStatus(index){
    var request = new XMLHttpRequest();
    request.open('POST', '/changeTaskStatus', true);
    request.setRequestHeader("Content-type", "application/json");
    request.send('{ "data" : "' + index + '" }'); //send the request

}
// Add a "checked" symbol when clicking on a checkbox and remove it when unchecking it
function toggleChecked(){
      	if (this.checked === true){
        	this.parentElement.classList.toggle('checked');
            changeTaskStatus(this.id);
	    }else{
		this.parentElement.classList.remove('checked');
		changeTaskStatus(this.id);
	    }
	}
//	location.reload();
//    getRetrieve();



// Create a new list item when clicking on the "+" button
function newElement() {
  var li = document.createElement("li");
  var inputValue = prompt("What's Your Mission?");
  if (inputValue === '' || inputValue == null) {
    alert("You must write something!");
    return;
  }

    new listItem(inputValue, index, false).getHtml();
    index += 1;
  var request = new XMLHttpRequest();
  request.open('POST', '/addElement', true);
  request.setRequestHeader("Content-type", "application/json");
  request.send('{ "data" : "' + inputValue + '" }'); //send the request
}


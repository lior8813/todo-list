function ListItem(name, id, isDone){
    this.name = name;
    this.id = id;
    this.isDone = isDone;
    this.getHtml = function(){
        var li = document.createElement("li");
        var t = document.createTextNode(this.name);
        li.appendChild(t);
        var cb = document.createElement( "input" );
        cb.type = "checkbox";
            //cb.value = value;
            //value = value + 1;
            //cb.class="squaredOne";
            //cb.padding= 10px;
        cb.onchange	= toggleChecked;
        cb.checked = false;
        cb.id = document.getElementById("myUL").getElementsByTagName("li").length;
        li.appendChild(cb);
        document.getElementById("myUL").appendChild(li);
        var span = document.createElement("SPAN");
        var txt = document.createTextNode("\u00D7");
        span.className = "close";
        span.appendChild(txt);
        li.appendChild(span);
        for (i = 0; i < close.length; i++) {
            close[i].onclick = function() {
                var div = this.parentElement;
                div.style.display = "none";
            }
        }
    }
}

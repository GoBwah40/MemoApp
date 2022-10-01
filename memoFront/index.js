 const div = document.getElementById("ici");
 const URLBACKEND = 'http://localhost:8080/'

 function addMemo(id = null, title = "", content = "") {
     if (id == null) {
         fetch(URLBACKEND, {
             method: 'POST',
         }).then((resp) => {
             return resp.json();
         }).then((json) => {
             creationMemo(json, "", "");
             generatePopUP("Nouveau memo créer avec succès");
         }).catch((error) => {
             console.error("Creation : " + error);
             alert("Une erreur est survenue")
         });
     } else {
         creationMemo(id, title, content);
     }
 }

 function creationMemo(id, title, content) {
     const memos = document.querySelectorAll(".memo");

     let newMemo = document.createElement("div");
     newMemo.classList.add("memo");
     newMemo.id = id || memos.length;
     newMemo.innerHTML += `
    <div class="headerMemo">
    <h2><input type="text" placeholder="Titre" id="title-${newMemo.id}" value="${title}"></h2>
    <input type="checkbox" name="ok" id="checkbox-${newMemo.id}" onchange="modifButton(${newMemo.id},this.checked)">
    </div>
    
    <div class="memoContainer">
    <textarea placeholder="Contenu" id="textMemo-${newMemo.id}">${content}</textarea>
    </div>
    
    <div class="maj">
    <button class="valider" onclick="validate(${newMemo.id})" id="valider-${newMemo.id}">Modifier</button>
    <button class="valider" onclick="deleteMemo(${newMemo.id})">Supprimer</button>
    </div>`;
     div.append(newMemo);
 }

 function deleteMemo(id) {
     const checkbox = document.getElementById("checkbox-" + id);
     if (checkbox.checked) {
         fetch(URLBACKEND + id, { method: 'DELETE' }).then((resp) => {
             if (resp.status == 200) {
                 const memo = document.getElementById(id);
                 memo.remove();
                 generatePopUP("Memo supprimé avec succès !")
             } else {
                 alert("Une erreur est survenue");
             }
         }).catch((error) => {
             console.error("delete memo: " + error);
             alert("Une erreur est survenue")
         });
     } else {
         alert("Veuillez cocher la case pour supprimer");
     }
 }

 function modifButton(id, isChecked) {
     const button = document.getElementById("valider-" + id);
     const texteArea = document.getElementById(`textMemo-` + id);
     const title = document.getElementById(`title-` + id);
     if (isChecked) {
         button.disable = !isChecked;
         texteArea.readOnly = false;
         title.disabled = false;
     } else {
         texteArea.readOnly = true;
         title.disabled = true;
     }
 }

 function validate(id) {

     const checkbox = document.getElementById("checkbox-" + id);
     if (!checkbox.checked) {
         alert("Veuillez cocher la case");
         return;
     }
     const title = document.getElementById(`title-` + id);
     const texteArea = document.getElementById(`textMemo-` + id);
     fetch(URLBACKEND + id, {
         headers: {
             'Accept': 'application/json',
             'Content-Type': 'application/json'
         },
         method: 'PUT',
         body: JSON.stringify({ title: title.value, content: texteArea.value })
     }).then((resp) => {
         if (resp.status == 200) {
             modification(id);
         } else {
             alert("Une erreur est survenue");
         }
     }).catch((error) => {
         console.error("Modification : " + error);
         alert("Une erreur est survenue")
     });

 }

 function modification(id) {
     const checkbox = document.getElementById("checkbox-" + id);
     const title = document.getElementById(`title-` + id);
     const texteArea = document.getElementById(`textMemo-` + id);
     checkbox.checked = false;
     texteArea.readOnly = true;
     title.disabled = true;

     generatePopUP("Modification memo faite avec succès");
 }

 fetch(URLBACKEND).then((resp) => {
     return resp.json();
 }).then((json) => {
     for (let memo of json) {
         addMemo(memo.id, memo.title, memo.description);
     }
 }).catch((error) => {
     console.error("Get all: " + error);
     alert("Une erreur est survenue")
 });

 function generatePopUP(message) {
     const body = document.getElementsByTagName("body");
     let popUp = document.createElement("div");
     popUp.innerHTML = `${message}`;
     popUp.className = "popUp";
     body[0].append(popUp);
     setTimeout(() => {
         popUp.className = "popUpCiao";
         setTimeout(() => {
             popUp.remove();
         }, 1000);
     }, 1000)
 }
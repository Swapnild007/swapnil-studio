const places=[
{id:1,name:"Shrimant Dagdusheth Halwai Ganpati",mr:"श्रीमंत दगडूशेठ हलवाई गणपती",area:"Budhwar Peth",type:"Famous",lat:18.5167,lng:73.8562,rank:"Iconic"},
{id:2,name:"Shri Kasba Ganpati",mr:"श्री कसबा गणपती",area:"Kasba Peth",type:"Manache Paach",lat:18.5206,lng:73.8560,rank:"#1"},
{id:3,name:"Tambdi Jogeshwari Ganpati",mr:"तांबडी जोगेश्वरी गणपती",area:"Budhwar Peth",type:"Manache Paach",lat:18.5160,lng:73.8550,rank:"#2"},
{id:4,name:"Guruji Talim Ganpati",mr:"गुरुजी तालीम गणपती",area:"Budhwar Peth",type:"Manache Paach",lat:18.5151,lng:73.8541,rank:"#3"},
{id:5,name:"Tulshibaug Ganpati",mr:"तुळशीबाग गणपती",area:"Budhwar Peth",type:"Manache Paach",lat:18.5128,lng:73.8563,rank:"#4"},
{id:6,name:"Kesariwada Ganpati",mr:"केसरीवाडा गणपती",area:"Narayan Peth",type:"Manache Paach",lat:18.5184,lng:73.8507,rank:"#5"},
{id:7,name:"Akhil Mandai Mandal",mr:"अखिल मंडई मंडळ",area:"Shukrawar Peth",type:"Famous",lat:18.5134,lng:73.8555,rank:"Famous"},
{id:8,name:"Chhatrapati Rajaram Mandal",mr:"छत्रपती राजाराम मंडळ",area:"Sadashiv Peth",type:"Famous",lat:18.5108,lng:73.8485,rank:"Famous"},
{id:9,name:"Shri Siddhivinayak, Sarasbaug",mr:"श्री सिद्धिविनायक, सारसबाग",area:"Sadashiv Peth",type:"Temple",lat:18.4974,lng:73.8546,rank:"Temple"},
{id:10,name:"Shri Morya Gosavi Ganpati Mandir",mr:"श्री मोरया गोसावी गणपती मंदिर",area:"Chinchwad",type:"Temple",lat:18.6298,lng:73.7997,rank:"Temple"},
{id:11,name:"Shrimant Bhausaheb Rangari Ganpati",mr:"श्रीमंत भाऊसाहेब रंगारी गणपती",area:"Budhwar Peth",type:"Historic",lat:18.5158,lng:73.8509,rank:"Historic"},
{id:12,name:"Hutatma Babu Genu Mandal",mr:"हुतात्मा बाबू गेनू मंडळ",area:"Budhwar Peth",type:"Historic",lat:18.5164,lng:73.8519,rank:"Historic"}
];
const routes=[
["Dagdusheth & the Manache Paach","6 stops","about 3 hr 8 min","Five ceremonial stops with Dagdusheth on the way."],
["Manache 5 Sakal Walk","5 stops","about 1 hr 55 min","All five Manache Paach before the peths fill up."],
["90-minute peth express","4 stops","about 1 hr 16 min","A compact walk for a short evening."],
["Historic peth stroll","5 stops","about 1 hr 48 min","Historic mandals, wadas and talims."]
];
const $=s=>document.querySelector(s), $$=s=>[...document.querySelectorAll(s)];
let activeTab="home",filter="All",saved=JSON.parse(localStorage.getItem("puneSaved")||"[]"),map=null,markers=[],userPos=null;
function esc(v){return String(v??"").replace(/[&<>"']/g,m=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[m]))}
function showTab(tab){activeTab=tab==="map"?"mapScreen":tab; $$(".screen").forEach(x=>x.classList.toggle("active",x.id===activeTab));$$(".nav-item").forEach(x=>x.classList.toggle("active",x.dataset.tab===tab));window.scrollTo({top:0,behavior:"smooth"});if(tab==="map")setTimeout(initMap,60)}
function toast(t){const x=$("#toast");x.textContent=t;x.classList.add("show");setTimeout(()=>x.classList.remove("show"),2200)}
function placeCard(p){return `<article class="place" data-id="${p.id}"><span class="num">${esc(p.rank)}</span><h3>${esc(p.name)}</h3><p>${esc(p.area)} · ${esc(p.type)}</p></article>`}
function exploreItem(p){return `<div class="explore-item" data-id="${p.id}"><span class="avatar">ॐ</span><div><b>${esc(p.name)}</b><small>${esc(p.mr)} · ${esc(p.area)}</small></div><button class="save" data-save="${p.id}">${saved.includes(p.id)?"♥":"♡"}</button></div>`}
function renderHome(){ $("#routeCards").innerHTML=routes.map(r=>`<article class="route-card"><div><span class="eyebrow">DARSHAN MARG</span><h3>${r[0]}</h3><p>${r[3]}</p></div><span class="route-meta">${r[1]} · ${r[2]}</span></article>`).join("");$("#featured").innerHTML=places.slice(0,8).map(placeCard).join("");$("#manache").innerHTML=places.slice(1,6).map(p=>`<button data-id="${p.id}"><strong>${p.rank}</strong><b>${esc(p.name.replace(" Ganpati",""))}</b><small>${esc(p.area)}</small></button>`).join("")}
function renderExplore(){const types=["All","Famous","Manache Paach","Historic","Temple"];$("#filters").innerHTML=types.map(t=>`<button class="filter ${filter===t?"active":""}" data-filter="${t}">${t}</button>`).join("");const q=($("#search")?.value||"").toLowerCase();const list=places.filter(p=>(filter==="All"||p.type===filter)&&(p.name+" "+p.mr+" "+p.area).toLowerCase().includes(q));$("#exploreList").innerHTML=list.map(exploreItem).join("")||'<div class="source-card">No matching places yet.</div>'}
function renderSaved(){const list=places.filter(p=>saved.includes(p.id));$("#savedList").innerHTML=list.length?list.map(exploreItem).join(""):'<div class="source-card"><b>Nothing saved yet.</b><p>Tap ♡ on any place to keep it here.</p></div>'}
function openPlace(id){const p=places.find(x=>x.id===Number(id));if(!p)return;$("#placeDetail").innerHTML=`<div class="page-head" style="padding:35px 0 10px"><span class="eyebrow">${esc(p.type.toUpperCase())}</span><h1 style="font-size:44px">${esc(p.name)}</h1><p>${esc(p.mr)}<br>${esc(p.area)}, Pune</p></div><div class="source-card"><b>Plan a visit</b><p>Use the map to see this place, save it, or open walking directions.</p><div style="display:flex;gap:8px;margin-top:12px"><button class="primary" style="flex:1" onclick="openDirections(${p.lat},${p.lng})">Directions →</button><button class="choice" onclick="toggleSave(${p.id})">${saved.includes(p.id)?"♥ Saved":"♡ Save"}</button></div></div>`;$("#placeModal").classList.add("open")}
function toggleSave(id){id=Number(id);saved=saved.includes(id)?saved.filter(x=>x!==id):[...saved,id];localStorage.setItem("puneSaved",JSON.stringify(saved));renderExplore();renderSaved();toast(saved.includes(id)?"Saved to your Pune":"Removed from saved")}
let activeDestination=null,routeGeoJSON=null;
async function openDirections(lat,lng){
  activeDestination=[lng,lat];
  const p=places.find(x=>Math.abs(x.lat-lat)<0.00001&&Math.abs(x.lng-lng)<0.00001);
  $("#directionTitle").textContent=p?.name||"Walking directions";
  $("#directionSummary").textContent="Getting your current location…";
  $("#directionSteps").innerHTML='<div class="direction-loading"><span></span><span></span><span></span></div>';
  $("#directionModal").classList.add("open");
  if(!navigator.geolocation){
    $("#directionSummary").textContent="Location is not available. Tap Near me on the map first.";
    $("#directionSteps").innerHTML="";
    return;
  }
  navigator.geolocation.getCurrentPosition(async pos=>{
    const from=[pos.coords.longitude,pos.coords.latitude];
    try{
      const u="https://routing.openstreetmap.de/routed-foot/route/v1/driving/"+from[0]+","+from[1]+";"+lng+","+lat+"?overview=full&geometries=geojson&steps=true";
      const r=await fetch(u);
      if(!r.ok)throw new Error("Routing service unavailable");
      const d=await r.json();
      const route=d.routes?.[0];
      if(!route)throw new Error("No walking route found");
      routeGeoJSON=route.geometry;
      const kmValue=route.distance/1000;
      const km=kmValue.toFixed(1);
      // Some public OSRM foot instances can return an implausibly short duration.
      // Keep the route geometry/steps from the router, but calculate a sane walking ETA.
      const routerMins=Number(route.duration)/60;
      const walkingMins=Math.max(1,Math.round(kmValue/5*60));
      const mins=(routerMins/kmValue>=3 && routerMins/kmValue<=20)
        ? Math.max(1,Math.round(routerMins))
        : walkingMins;
      $("#directionSummary").innerHTML=`<b>${mins} min</b><span>·</span><b>${km} km</b><span>·</span><span>Walking · estimated</span>`;
      const maneuverText=(m)=>{
        const type=m?.type||"";
        const mod=m?.modifier||"";
        if(type==="depart") return "Start walking";
        if(type==="arrive") return "You have arrived";
        if(type==="roundabout"||type==="rotary") return "Enter the roundabout";
        const map={
          left:"Turn left",
          right:"Turn right",
          straight:"Continue straight",
          "slight left":"Bear left",
          "slight right":"Bear right",
          "sharp left":"Sharp left",
          "sharp right":"Sharp right",
          uturn:"Make a U-turn"
        };
        return map[mod]||"Continue";
      };
      const steps=route.legs?.[0]?.steps||[];
      $("#directionSteps").innerHTML=steps.slice(0,12).map((s,i)=>{
        const name=s.name||"Unnamed road";
        const maneuver=maneuverText(s.maneuver);
        return `<div class="direction-step"><span class="step-num">${i+1}</span><div><b>${esc(maneuver)}</b><small>${esc(name)} · ${Math.round(s.distance)} m</small></div></div>`;
      }).join("")||'<div class="direction-step"><span class="step-num">✓</span><div><b>Follow the route to your destination</b></div></div>';
    }catch(e){
      $("#directionSummary").textContent=e.message||"Could not build a walking route.";
      $("#directionSteps").innerHTML="";
    }
  },()=>{
    $("#directionSummary").textContent="Location permission is needed to calculate directions from you.";
    $("#directionSteps").innerHTML="";
  },{enableHighAccuracy:true,timeout:10000,maximumAge:30000});
}
function showRouteOnMap(){
  if(!routeGeoJSON||!activeDestination){toast("Route is not ready yet.");return}
  $("#directionModal").classList.remove("open");
  showTab("map");
  setTimeout(()=>{
    if(!map)return;
    if(map.getSource("walking-route"))map.removeLayer("walking-route"),map.removeSource("walking-route");
    map.addSource("walking-route",{type:"geojson",data:{type:"Feature",geometry:routeGeoJSON}});
    map.addLayer({id:"walking-route",type:"line",source:"walking-route",paint:{"line-color":"#007aff","line-width":6,"line-opacity":.88,"line-blur":.5}});
    const coords=routeGeoJSON.coordinates;
    const bounds=coords.reduce((b,c)=>b.extend(c),new maplibregl.LngLatBounds(coords[0],coords[0]));
    map.fitBounds(bounds,{padding:70,maxZoom:16,duration:900});
    toast("Walking route shown on the map");
  },100);
}
function initMap(){if(map)return;map=new maplibregl.Map({container:"map",style:"https://tiles.openfreemap.org/styles/liberty",center:[73.8567,18.5204],zoom:13.1});map.addControl(new maplibregl.NavigationControl({showCompass:false}),"bottom-right");map.on("load",()=>{places.forEach(p=>addMarker(p));renderMapList()})}
function addMarker(p){const el=document.createElement("button");el.className="map-pin";el.textContent="ॐ";el.style.cssText="width:34px;height:34px;border-radius:12px;border:2px solid white;background:#b85b25;color:white;box-shadow:0 7px 20px #0004;font-size:16px;cursor:pointer";el.onclick=()=>openPlace(p.id);new maplibregl.Marker({element:el}).setLngLat([p.lng,p.lat]).addTo(map)}
function renderMapList(){$("#mapList").innerHTML=places.slice(0,6).map(exploreItem).join("")}
function locate(){if(!navigator.geolocation){toast("Location is not available in this browser.");return}navigator.geolocation.getCurrentPosition(pos=>{userPos=[pos.coords.longitude,pos.coords.latitude];if(map){map.flyTo({center:userPos,zoom:15});new maplibregl.Marker({color:"#211e19"}).setLngLat(userPos).addTo(map)}toast("Showing places near you")},()=>toast("Location permission was not granted."))}
function buildPlan(){const min=Number($(".choice.active[data-min]")?.dataset.min||90);const pref=$(".choice.active[data-pref]")?.dataset.pref||"famous";let pool=pref==="manache"?places.slice(1,6):pref==="historic"?places.filter(p=>p.type==="Historic"):pref==="quiet"?places.slice(1,5):places.filter(p=>["Famous","Manache Paach"].includes(p.type));const count=min<=90?3:min<=120?4:5;pool=pool.slice(0,count);$("#planResult").innerHTML=`<div class="plan-card"><span class="eyebrow">YOUR ${min>=120?min/60+" HOUR":"90 MINUTE"} PLAN</span><h2 style="font:500 29px Georgia,serif;margin:8px 0 14px">${pool.length} stops · walkable evening</h2>${pool.map((p,i)=>`<div class="plan-stop"><strong>0${i+1}</strong><div><b>${esc(p.name)}</b><small>${esc(p.area)} · ${i===0?"Start here":"Next stop"}</small></div></div>`).join("")}<button class="primary" style="margin-top:10px" onclick="showTab('map')">Open in map →</button></div>`;$("planResult").scrollIntoView({behavior:"smooth"})}
document.addEventListener("click",e=>{const tab=e.target.closest("[data-tab]");if(tab){e.preventDefault();showTab(tab.dataset.tab)}const f=e.target.closest("[data-filter]");if(f){filter=f.dataset.filter;renderExplore()}const p=e.target.closest("[data-id]");if(p&&!e.target.closest("[data-save]"))openPlace(p.dataset.id);const s=e.target.closest("[data-save]");if(s){e.stopPropagation();toggleSave(s.dataset.save)}})
$("#openSearch").onclick=()=>{$("#searchModal").classList.add("open");setTimeout(()=>$("#modalSearch").focus(),50)}
$("#closeSearch").onclick=()=>$("#searchModal").classList.remove("open");$("#searchModal .modal-backdrop").onclick=()=>$("#searchModal").classList.remove("open");$("#closePlace").onclick=()=>$("#placeModal").classList.remove("open");$("#placeModal .modal-backdrop").onclick=()=>$("#placeModal").classList.remove("open");$("#closeDirections").onclick=()=>$("#directionModal").classList.remove("open");$("#directionModal .modal-backdrop").onclick=()=>$("#directionModal").classList.remove("open");$("#openRouteMap").onclick=showRouteOnMap;$("#lang").onclick=()=>toast("मराठी interface is coming next");$("#sources").onclick=()=>toast("OpenFreeMap + OpenStreetMap • community data clearly labelled");$("#locate").onclick=locate;$("#myLocation").onclick=locate;$("#buildPlan").onclick=buildPlan;
$("#timeChoices").onclick=e=>{const b=e.target.closest(".choice");if(!b)return;$$("#timeChoices .choice").forEach(x=>x.classList.remove("active"));b.classList.add("active")};
$(".planner").onclick=e=>{const b=e.target.closest("[data-pref]");if(!b)return;$$("[data-pref]").forEach(x=>x.classList.remove("active"));b.classList.add("active")};
$("#search").oninput=renderExplore;
$("#modalSearch").oninput=e=>{const q=e.target.value.toLowerCase();const r=places.filter(p=>(p.name+" "+p.mr+" "+p.area).toLowerCase().includes(q)).slice(0,7);$("#searchResults").innerHTML=r.map(exploreItem).join("")||"<p style='color:var(--muted);padding:15px'>Start typing to search Pune.</p>"};
renderHome();renderExplore();renderSaved();
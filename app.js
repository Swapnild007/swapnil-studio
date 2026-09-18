const map=L.map('map',{zoomControl:false,preferCanvas:true}).setView([18.5204,73.8567],12);L.control.zoom({position:'bottomright'}).addTo(map);L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',{maxZoom:19,attribution:'© OpenStreetMap'}).addTo(map);

let flow=null;
let flowLoaded=false;
const markers=L.layerGroup().addTo(map);
const bbox='73.70,18.40,74.05,18.75';
const AUTO_REFRESH_MS=0;
const q=s=>document.querySelector(s);
const esc=v=>String(v??'').replace(/[&<>"']/g,m=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]));

function setState(text,offline=false){
  q('#state').textContent=text;
  q('.live-state').classList.toggle('offline',offline);
}

async function incidents(){
  try{
    const r=await fetch('/api/incidents?bbox='+bbox,{cache:'no-store'});
    if(!r.ok){
      let msg='Live incident feed unavailable';
      try{const e=await r.json();if(e.error)msg=e.error}catch{}
      throw new Error(msg);
    }
    const d=await r.json(),a=Array.isArray(d.incidents)?d.incidents:[];
    markers.clearLayers();
    let c=0;
    a.forEach(x=>{
      const p=x.geometry?.coordinates;
      if(!p||typeof p[0]!=='number'||typeof p[1]!=='number')return;
      const cat=x.properties?.iconCategory||'incident';
      if(cat==='roadClosed'||cat==='laneClosed')c++;
      L.marker([p[1],p[0]]).bindPopup('<b>'+esc(cat.replace(/([A-Z])/g,' $1'))+'</b><br><span>Live traffic event</span>').addTo(markers);
    });
    q('#inc').textContent=a.length;
    q('#closed').textContent=c;
    q('#list').innerHTML=a.slice(0,10).map(x=>{
      const cat=(x.properties?.iconCategory||'incident').replace(/([A-Z])/g,' $1');
      return '<div class="incident"><b>'+esc(cat)+'</b><span>Live traffic event</span></div>';
    }).join('')||'No current incidents returned.';
    setState('MANUAL');
  }catch(e){
    setState('OFFLINE',true);
    q('#list').textContent=e.message||'Live feed unavailable.';
  }
}

function refresh(){
  flowLoaded=false;
  if(flow)map.removeLayer(flow);
  flow=L.tileLayer('/api/flow/{z}/{x}/{y}?style=light&tileSize=256',{
    opacity:.96,
    maxZoom:19,
    minZoom:8,
    updateWhenIdle:true,
    keepBuffer:2,
    zIndex:450
  });
  flow.on('load',()=>{flowLoaded=true;setState('MANUAL')});
  flow.on('tileerror',()=>{if(!flowLoaded)setState('MANUAL')});
  flow.addTo(map);
  q('#updated').textContent=new Date().toLocaleTimeString();
  incidents();
}

async function road(lat,lng){
  q('#road').textContent='Loading…';
  q('#details').textContent='Reading live road segment…';
  try{
    const r=await fetch('/api/flow-segment?lat='+encodeURIComponent(lat)+'&lng='+encodeURIComponent(lng),{cache:'no-store'});
    if(!r.ok)throw new Error('Live road detail unavailable.');
    const f=(await r.json()).flowSegmentData;
    if(!f)throw new Error('No traffic segment returned.');
    const current=Number(f.currentSpeed);
    const free=Number(f.freeFlowSpeed);
    const red=free>0?Math.max(0,Math.round((1-current/free)*100)):0;
    const level=red>=65?'Severe':red>=40?'Heavy':red>=15?'Slow':'Free flow';
    q('#road').textContent='Selected road';
    q('#details').innerHTML='<span class="traffic-value">'+esc(current)+' km/h</span> current · '+esc(free)+' km/h free-flow<br>Speed reduction: <b>'+red+'%</b> <span class="traffic-badge">'+level+'</span><br>Travel time: '+esc(f.currentTravelTime)+' sec · Confidence: '+Math.round(Number(f.confidence||0)*100)+'%<br>Closure: <b>'+(f.roadClosure?'YES':'No')+'</b>';
  }catch(e){
    q('#details').textContent=e.message||'Live road detail unavailable.';
  }
}

map.on('click',e=>road(e.latlng.lat,e.latlng.lng));
q('#refresh').onclick=refresh;
refresh();
if(AUTO_REFRESH_MS>0)setInterval(refresh,AUTO_REFRESH_MS);
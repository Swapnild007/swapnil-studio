export default async function handler(req,res){
  const k=process.env.TOMTOM_API_KEY;
  if(!k)return res.status(503).json({error:'TOMTOM_API_KEY missing'});
  const {z,x,y}=req.query;
  const style=req.query.style==='dark'?'dark':'light';
  const u='https://api.tomtom.com/maps/orbis/traffic/flow/raster/tile/'+z+'/'+x+'/'+y+'?apiVersion=2&style='+style+'&tileSize=256&key='+encodeURIComponent(k);
  try{
    const r=await fetch(u,{headers:{'TomTom-Api-Key':k,'TomTom-Api-Version':'2','Accept':'image/png'},cache:'no-store'});
    const body=Buffer.from(await r.arrayBuffer());
    res.status(r.status);
    res.setHeader('Cache-Control','no-store');
    res.setHeader('Content-Type',r.ok?'image/png':'application/json');
    return res.send(body);
  }catch(e){
    return res.status(502).json({error:'TomTom unavailable'});
  }
}
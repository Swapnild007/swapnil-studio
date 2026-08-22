import OpenAI, { toFile } from "openai";

const client = new OpenAI({
  apiKey: process.env.OPENAI_API_KEY
});

const PRICES = {
  "1024x1024": 0.04,
  "1024x1536": 0.08,
  "1536x1024": 0.08
};

const prompts = {
  "Bust Editorial":
    "Create a sophisticated high-fashion editorial portrait emphasizing the upper-body silhouette. Preserve the person's recognizable facial appearance, natural proportions and overall identity. Photorealistic professional fashion photography.",

  "Hip-Line Editorial":
    "Create a sophisticated fashion editorial emphasizing the waist-to-hip silhouette through tasteful composition, clothing, pose and lighting. Keep the result non-explicit, photorealistic and suitable for a fashion magazine.",

  "Waist Silhouette":
    "Create a high-fashion editorial composition emphasizing the waist and natural silhouette through elegant posing, tailored styling and cinematic lighting. Photorealistic and non-explicit.",

  "Over-the-Shoulder Editorial":
    "Create a sophisticated over-the-shoulder fashion editorial portrait with cinematic professional lighting and magazine-quality composition. Preserve recognizable facial appearance and natural proportions.",

  "Seated Editorial":
    "Create a sophisticated seated fashion editorial photograph with elegant posture, premium styling and cinematic studio lighting. Photorealistic, tasteful and magazine quality.",

  "Full-Length Fashion":
    "Create a full-length high-fashion editorial photograph with sophisticated styling, professional studio lighting and natural body proportions. Photorealistic magazine photography.",

  "Back-Drape Editorial":
    "Create an elegant fashion editorial from a rear three-quarter composition with sophisticated draping, cinematic lighting and magazine-quality photography. Tasteful and non-explicit.",

  "Boudoir-Inspired Editorial":
    "Create a sophisticated luxury editorial inspired by intimate fashion photography, using elegant styling, dramatic lighting and tasteful composition. Non-explicit fashion photography.",

  "Intimate Fashion Editorial":
    "Create a sophisticated intimate fashion editorial with elegant styling, cinematic lighting and refined magazine composition. Non-explicit and photorealistic.",

  "Dark Luxury":
    "Transform the photograph into a dark luxury fashion editorial with dramatic cinematic lighting, premium styling, deep shadows and polished magazine photography.",

  "Wet-Gloss Fashion":
    "Create a glossy high-fashion editorial aesthetic with reflective highlights, sophisticated styling and controlled studio lighting. Keep the result tasteful and non-explicit.",

  "After Dark":
    "Create an after-dark luxury fashion editorial with dramatic studio lighting, deep contrast, sophisticated styling and cinematic magazine photography.",

  "Cinematic Fashion":
    "Create a cinematic high-fashion editorial photograph with dramatic professional lighting, rich depth and premium magazine styling.",

  "Magazine Editorial":
    "Create a premium high-fashion magazine editorial photograph with sophisticated styling, professional lighting and polished photographic composition."
};

export default async function handler(req, res) {
  if (req.method !== "POST") {
    return res.status(405).json({
      error: "Method not allowed"
    });
  }

  try {
    const {
      image,
      effect = "Dark Luxury",
      composition = "Three-quarter editorial",
      mood = "Cinematic warm",
      size = "1024x1536"
    } = req.body || {};

    if (!image) {
      return res.status(400).json({
        error: "Picture A is required."
      });
    }

    if (!process.env.OPENAI_API_KEY) {
      return res.status(500).json({
        error: "OPENAI_API_KEY is not configured in Vercel."
      });
    }

    const match = image.match(/^data:(image\/[a-zA-Z0-9.+-]+);base64,(.+)$/);

    if (!match) {
      return res.status(400).json({
        error: "Invalid image format."
      });
    }

    const mime = match[1];
    const base64 = match[2];

    const extension =
      mime.includes("jpeg") || mime.includes("jpg")
        ? "jpg"
        : mime.includes("webp")
        ? "webp"
        : "png";

    const input = await toFile(
      Buffer.from(base64,

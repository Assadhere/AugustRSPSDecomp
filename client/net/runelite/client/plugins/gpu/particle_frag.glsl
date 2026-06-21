/*
 * Particle fragment shader
 * Supports tiered texture arrays for optimal memory usage:
 * - Tier 0: 64x64 textures
 * - Tier 1: 128x128 textures
 * - Tier 2: 256x256 textures
 * - Tier 3: 1024x1024 textures
 */
#version 330

// One sampler per tier
uniform sampler2DArray particleTex64;
uniform sampler2DArray particleTex128;
uniform sampler2DArray particleTex256;
uniform sampler2DArray particleTex1024;

in vec4 fColor;
flat in int fTextureId;  // Encoded: (tier << 14) | indexInTier
in vec2 fUv;

out vec4 FragColor;

void main() {
  vec4 color = fColor;

  if (fTextureId > 0) {
    // Decode tier and index from texture ID
    // Subtract 1 because renderer adds 1 to distinguish from "no texture" (0)
    int encodedId = fTextureId - 1;
    int tier = (encodedId >> 14) & 0x3;
    int texIdx = encodedId & 0x3FFF;

    vec4 texColor;
    if (tier == 0) {
      texColor = texture(particleTex64, vec3(fUv, float(texIdx)));
    } else if (tier == 1) {
      texColor = texture(particleTex128, vec3(fUv, float(texIdx)));
    } else if (tier == 2) {
      texColor = texture(particleTex256, vec3(fUv, float(texIdx)));
    } else {
      texColor = texture(particleTex1024, vec3(fUv, float(texIdx)));
    }

    color = texColor * color;
  }

  if (color.a < 0.01) {
    discard;
  }

  FragColor = color;
}

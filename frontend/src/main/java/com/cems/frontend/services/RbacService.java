package com.cems.frontend.services;

public class RbacService {
    public static String getRoleFromToken(String token) {
        try {
            // JWT has 3 parts: header.payload.signature
            String[] parts = token.split("\\.");
            if (parts.length != 3) throw new IllegalArgumentException("Invalid JWT");

            // Decode the payload (index 1)
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));

            // Parse JSON payload
            JSONOb json = new JSONObject(payload);

            // Extract role — adjust key name to match your token
            return json.getString("role"); // or "roles", "authorities", etc.

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

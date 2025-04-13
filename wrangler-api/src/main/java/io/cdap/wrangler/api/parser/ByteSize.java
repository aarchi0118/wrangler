/*
 * Copyright © [year] [your organization or name].
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


 package io.cdap.wrangler.api.parser;

 import com.google.gson.JsonElement;
 import com.google.gson.JsonPrimitive;
 
 /**
  * Represents a parsed ByteSize token (e.g., "10MB", "512KB").
  */
 public class ByteSize implements Token {
   private final long bytes;
   private final String original;
 
   public ByteSize(String value) {
     this.original = value;
     this.bytes = parseBytes(value);
   }
 
   private long parseBytes(String value) {
     value = value.toUpperCase().trim();
     if (value.endsWith("KB")) {
       return (long) (Double.parseDouble(value.replace("KB", "")) * 1024);
     } else if (value.endsWith("MB")) {
       return (long) (Double.parseDouble(value.replace("MB", "")) * 1024 * 1024);
     } else if (value.endsWith("GB")) {
       return (long) (Double.parseDouble(value.replace("GB", "")) * 1024 * 1024 * 1024);
     } else {
       throw new IllegalArgumentException("Invalid ByteSize format: " + value);
     }
   }
 
   public long getBytes() {
     return bytes;
   }
 
   @Override
   public Object value() {
     return bytes;
   }
 
   @Override
   public TokenType type() {
     return TokenType.BYTESIZE;
   }
 
   @Override
   public JsonElement toJson() {
     return new JsonPrimitive(bytes);
   }
 }
 
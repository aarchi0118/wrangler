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
  * Represents a parsed TimeDuration token (e.g., "500ms", "2s", "1.5h").
  */
 public class TimeDuration implements Token {
   private final long milliseconds;
   private final String original;
 
   public TimeDuration(String value) {
     this.original = value;
     this.milliseconds = parseMilliseconds(value);
   }
 
   private long parseMilliseconds(String value) {
     value = value.toLowerCase().trim();
     if (value.endsWith("ms")) {
       return (long) Double.parseDouble(value.replace("ms", ""));
     } else if (value.endsWith("s")) {
       return (long) (Double.parseDouble(value.replace("s", "")) * 1000);
     } else if (value.endsWith("m")) {
       return (long) (Double.parseDouble(value.replace("m", "")) * 60 * 1000);
     } else if (value.endsWith("h")) {
       return (long) (Double.parseDouble(value.replace("h", "")) * 60 * 60 * 1000);
     } else {
       throw new IllegalArgumentException("Invalid TimeDuration format: " + value);
     }
   }
 
   public long getMilliseconds() {
     return milliseconds;
   }
 
   @Override
   public Object value() {
     return milliseconds;
   }
 
   @Override
   public TokenType type() {
     return TokenType.TIMEDURATION;
   }
 
   @Override
   public JsonElement toJson() {
     return new JsonPrimitive(milliseconds);
   }
 }
 
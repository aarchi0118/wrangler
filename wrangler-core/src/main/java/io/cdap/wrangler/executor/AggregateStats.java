/*
 * Copyright © 2024 YOUR NAME OR ORG
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

 

 package io.cdap.wrangler.executor;

 import io.cdap.wrangler.api.*;
 import io.cdap.wrangler.api.parser.*;

 
 import java.util.ArrayList;
 import java.util.List;
 
 public class AggregateStats implements Directive {
   private String sizeColumn;
   private String timeColumn;
   private String totalSizeCol;
   private String totalTimeCol;
 
   @Override
public UsageDefinition define() {
  UsageDefinition.Builder builder = UsageDefinition.builder("aggregate-stats");
  builder.define("sizeColumn", TokenType.COLUMN_NAME);
  builder.define("timeColumn", TokenType.COLUMN_NAME);
  builder.define("totalSizeCol", TokenType.COLUMN_NAME);
  builder.define("totalTimeCol", TokenType.COLUMN_NAME);
  return builder.build();
}

   
 
   @Override
   public void initialize(Arguments args) {
     sizeColumn = ((ColumnName) args.value("sizeColumn")).value();
     timeColumn = ((ColumnName) args.value("timeColumn")).value();
     totalSizeCol = ((ColumnName) args.value("totalSizeCol")).value();
     totalTimeCol = ((ColumnName) args.value("totalTimeCol")).value();
   }
 
   @Override
   public List<Row> execute(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException {
     long totalBytes = 0;
     long totalMillis = 0;
 
     for (Row row : rows) {
       Object sizeVal = row.getValue(sizeColumn);
       Object timeVal = row.getValue(timeColumn);
 
       if (sizeVal instanceof String) {
         totalBytes += new ByteSize((String) sizeVal).getBytes();
       }
 
       if (timeVal instanceof String) {
         totalMillis += new TimeDuration((String) timeVal).getMilliseconds();
       }
     }
 
     double totalMB = totalBytes / (1024.0 * 1024);
     double totalSec = totalMillis / 1000.0;
 
     Row result = new Row();
     result.add(totalSizeCol, totalMB);
     result.add(totalTimeCol, totalSec);
 
     List<Row> output = new ArrayList<>();
     output.add(result);
     return output;
   }
 
   @Override
   public void destroy() {
     // no cleanup
   }
 }
 
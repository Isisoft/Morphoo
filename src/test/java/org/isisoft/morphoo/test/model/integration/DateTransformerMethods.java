/*
 * Copyright 2010, Red Hat, Inc. and individual contributors as indicated by the
 * @author tags. See the copyright.txt file in the distribution for a full
 * listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.isisoft.morphoo.test.model.integration;

import org.isisoft.morphoo.annotation.ContextParam;
import org.isisoft.morphoo.annotation.Transformer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Carlos Munoz <a href="mailto:camunoz@redhat.com">camunoz@redhat.com</a>
 */
public class DateTransformerMethods
{
   @Transformer(isDefault = true)
   public String toLongString( Date date )
   {
      DateFormat format = DateFormat.getDateTimeInstance( DateFormat.LONG, DateFormat.LONG );
      return format.format(date);
   }

   @Transformer
   public String toMediumString( Date date )
   {
      DateFormat format = DateFormat.getDateTimeInstance( DateFormat.MEDIUM, DateFormat.MEDIUM );
      return format.format(date);
   }

   @Transformer
   public String toShortString( Date date )
   {
      DateFormat format = DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT );
      return format.format(date);
   }

   @Transformer
   public String toDateWithFormat( Date date, @ContextParam(name="format")String format )
   {
      if( format == null )
      {
         return this.toShortString(date);
      }
      else
      {
         DateFormat df = new SimpleDateFormat(format);
         return df.format(date);
      }
   }

}

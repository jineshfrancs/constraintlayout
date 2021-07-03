/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package androidx.constraintlayout.core.parser;

public class CLElement {

    private final char[] mContent;
    protected long start = -1;
    protected long end = Long.MAX_VALUE;
    protected CLContainer mContainer;
    private int line;

    protected static int MAX_LINE = 80; // Max number of characters before the formatter indents
    protected static int BASE_INDENT = 2; // default indentation value

    public CLElement(char[] content) {
        mContent = content;
    }

    public boolean notStarted() {
        return start == -1;
    }

    public void setLine(int line) {
        this.line = line;
    }

    /**
     * get the line Number
     *
     * @return return the line number this element was on
     */
    public int getLine() {
        return line;
    }

    public void setStart(long start) {
        this.start = start;
    }

    /**
     * The character index this element was started on
     *
     * @return
     */
    public long getStart() {
        return this.start;
    }

    /**
     * The character index this element was ended on
     *
     * @return
     */
    public long getEnd() {
        return this.end;
    }

    public void setEnd(long end) {
        if (this.end != Long.MAX_VALUE) {
            return;
        }
        this.end = end;
        if (CLParser.DEBUG) {
            System.out.println("closing " + this.hashCode() + " -> " + this);
        }
        if (mContainer != null) {
            mContainer.add(this);
        }
    }

    protected void addIndent(StringBuilder builder, int indent) {
        for (int i = 0; i < indent; i++) {
            builder.append(' ');
        }
    }

    @Override
    public String toString() {
        if (start > end || end == Long.MAX_VALUE) {
            return this.getClass() + " (INVALID, " + start + "-" + end + ")";
        }
        String content = new String(mContent);
        content = content.substring((int) start, (int) end + 1);

        return getStrClass() + " (" + start + " : " + end + ") <<" + content + ">>";
    }

    protected String getStrClass() {
        String myClass = this.getClass().toString();
        return myClass.substring(myClass.lastIndexOf('.') + 1);
    }

    protected String getDebugName() {
        if (CLParser.DEBUG) {
            return getStrClass() + " -> ";
        }
        return "";
    }

    public String content() {
        String content = new String(mContent);
        if (end == Long.MAX_VALUE || end < start) {
            return content.substring((int) start, (int) start + 1);
        }
        return content.substring((int) start, (int) end + 1);
    }

    public boolean isDone() {
        return end != Long.MAX_VALUE;
    }

    public void setContainer(CLContainer element) {
        mContainer = element;
    }

    public CLElement getContainer() {
        return mContainer;
    }

    public boolean isStarted() {
        return start > -1;
    }

    protected String toJSON() {
        return "";
    }

    protected String toFormattedJSON(int indent, int forceIndent) {
        return "";
    }

    public int getInt() {
        if (this instanceof CLNumber) {
            return ((CLNumber) this).getInt();
        }
        return 0;
    }

    public float getFloat() {
        if (this instanceof CLNumber) {
            return ((CLNumber) this).getInt();
        }
        return Float.NaN;
    }
}

// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: task/share.proto

package protocols.task;

public final class share {
  private share() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface requestOrBuilder extends
      // @@protoc_insertion_point(interface_extends:request)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * 分享的次数
     * </pre>
     *
     * <code>uint32 shareTimes = 1;</code>
     */
    int getShareTimes();
  }
  /**
   * <pre>
   * 请求
   * </pre>
   *
   * Protobuf type {@code request}
   */
  public  static final class request extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:request)
      requestOrBuilder {
    // Use request.newBuilder() to construct.
    private request(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private request() {
      shareTimes_ = 0;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private request(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 8: {

              shareTimes_ = input.readUInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return protocols.task.share.internal_static_request_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return protocols.task.share.internal_static_request_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              protocols.task.share.request.class, protocols.task.share.request.Builder.class);
    }

    public static final int SHARETIMES_FIELD_NUMBER = 1;
    private int shareTimes_;
    /**
     * <pre>
     * 分享的次数
     * </pre>
     *
     * <code>uint32 shareTimes = 1;</code>
     */
    public int getShareTimes() {
      return shareTimes_;
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (shareTimes_ != 0) {
        output.writeUInt32(1, shareTimes_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (shareTimes_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(1, shareTimes_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof protocols.task.share.request)) {
        return super.equals(obj);
      }
      protocols.task.share.request other = (protocols.task.share.request) obj;

      boolean result = true;
      result = result && (getShareTimes()
          == other.getShareTimes());
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + SHARETIMES_FIELD_NUMBER;
      hash = (53 * hash) + getShareTimes();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static protocols.task.share.request parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protocols.task.share.request parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protocols.task.share.request parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protocols.task.share.request parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protocols.task.share.request parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static protocols.task.share.request parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static protocols.task.share.request parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static protocols.task.share.request parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static protocols.task.share.request parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static protocols.task.share.request parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(protocols.task.share.request prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * <pre>
     * 请求
     * </pre>
     *
     * Protobuf type {@code request}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:request)
        protocols.task.share.requestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return protocols.task.share.internal_static_request_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return protocols.task.share.internal_static_request_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                protocols.task.share.request.class, protocols.task.share.request.Builder.class);
      }

      // Construct using protocols.task.share.request.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        shareTimes_ = 0;

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return protocols.task.share.internal_static_request_descriptor;
      }

      public protocols.task.share.request getDefaultInstanceForType() {
        return protocols.task.share.request.getDefaultInstance();
      }

      public protocols.task.share.request build() {
        protocols.task.share.request result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public protocols.task.share.request buildPartial() {
        protocols.task.share.request result = new protocols.task.share.request(this);
        result.shareTimes_ = shareTimes_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof protocols.task.share.request) {
          return mergeFrom((protocols.task.share.request)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(protocols.task.share.request other) {
        if (other == protocols.task.share.request.getDefaultInstance()) return this;
        if (other.getShareTimes() != 0) {
          setShareTimes(other.getShareTimes());
        }
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        protocols.task.share.request parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (protocols.task.share.request) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int shareTimes_ ;
      /**
       * <pre>
       * 分享的次数
       * </pre>
       *
       * <code>uint32 shareTimes = 1;</code>
       */
      public int getShareTimes() {
        return shareTimes_;
      }
      /**
       * <pre>
       * 分享的次数
       * </pre>
       *
       * <code>uint32 shareTimes = 1;</code>
       */
      public Builder setShareTimes(int value) {
        
        shareTimes_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 分享的次数
       * </pre>
       *
       * <code>uint32 shareTimes = 1;</code>
       */
      public Builder clearShareTimes() {
        
        shareTimes_ = 0;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:request)
    }

    // @@protoc_insertion_point(class_scope:request)
    private static final protocols.task.share.request DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new protocols.task.share.request();
    }

    public static protocols.task.share.request getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<request>
        PARSER = new com.google.protobuf.AbstractParser<request>() {
      public request parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new request(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<request> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<request> getParserForType() {
      return PARSER;
    }

    public protocols.task.share.request getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  public interface responseOrBuilder extends
      // @@protoc_insertion_point(interface_extends:response)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * 0 = 成功 其他 = 错误
     * </pre>
     *
     * <code>uint32 error = 1;</code>
     */
    int getError();

    /**
     * <pre>
     * 错误描述
     * </pre>
     *
     * <code>string errDesc = 2;</code>
     */
    java.lang.String getErrDesc();
    /**
     * <pre>
     * 错误描述
     * </pre>
     *
     * <code>string errDesc = 2;</code>
     */
    com.google.protobuf.ByteString
        getErrDescBytes();
  }
  /**
   * <pre>
   * 返回
   * </pre>
   *
   * Protobuf type {@code response}
   */
  public  static final class response extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:response)
      responseOrBuilder {
    // Use response.newBuilder() to construct.
    private response(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private response() {
      error_ = 0;
      errDesc_ = "";
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private response(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 8: {

              error_ = input.readUInt32();
              break;
            }
            case 18: {
              java.lang.String s = input.readStringRequireUtf8();

              errDesc_ = s;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return protocols.task.share.internal_static_response_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return protocols.task.share.internal_static_response_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              protocols.task.share.response.class, protocols.task.share.response.Builder.class);
    }

    public static final int ERROR_FIELD_NUMBER = 1;
    private int error_;
    /**
     * <pre>
     * 0 = 成功 其他 = 错误
     * </pre>
     *
     * <code>uint32 error = 1;</code>
     */
    public int getError() {
      return error_;
    }

    public static final int ERRDESC_FIELD_NUMBER = 2;
    private volatile java.lang.Object errDesc_;
    /**
     * <pre>
     * 错误描述
     * </pre>
     *
     * <code>string errDesc = 2;</code>
     */
    public java.lang.String getErrDesc() {
      java.lang.Object ref = errDesc_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        errDesc_ = s;
        return s;
      }
    }
    /**
     * <pre>
     * 错误描述
     * </pre>
     *
     * <code>string errDesc = 2;</code>
     */
    public com.google.protobuf.ByteString
        getErrDescBytes() {
      java.lang.Object ref = errDesc_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        errDesc_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (error_ != 0) {
        output.writeUInt32(1, error_);
      }
      if (!getErrDescBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, errDesc_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (error_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(1, error_);
      }
      if (!getErrDescBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, errDesc_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof protocols.task.share.response)) {
        return super.equals(obj);
      }
      protocols.task.share.response other = (protocols.task.share.response) obj;

      boolean result = true;
      result = result && (getError()
          == other.getError());
      result = result && getErrDesc()
          .equals(other.getErrDesc());
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + ERROR_FIELD_NUMBER;
      hash = (53 * hash) + getError();
      hash = (37 * hash) + ERRDESC_FIELD_NUMBER;
      hash = (53 * hash) + getErrDesc().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static protocols.task.share.response parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protocols.task.share.response parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protocols.task.share.response parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protocols.task.share.response parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protocols.task.share.response parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static protocols.task.share.response parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static protocols.task.share.response parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static protocols.task.share.response parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static protocols.task.share.response parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static protocols.task.share.response parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(protocols.task.share.response prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * <pre>
     * 返回
     * </pre>
     *
     * Protobuf type {@code response}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:response)
        protocols.task.share.responseOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return protocols.task.share.internal_static_response_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return protocols.task.share.internal_static_response_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                protocols.task.share.response.class, protocols.task.share.response.Builder.class);
      }

      // Construct using protocols.task.share.response.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        error_ = 0;

        errDesc_ = "";

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return protocols.task.share.internal_static_response_descriptor;
      }

      public protocols.task.share.response getDefaultInstanceForType() {
        return protocols.task.share.response.getDefaultInstance();
      }

      public protocols.task.share.response build() {
        protocols.task.share.response result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public protocols.task.share.response buildPartial() {
        protocols.task.share.response result = new protocols.task.share.response(this);
        result.error_ = error_;
        result.errDesc_ = errDesc_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof protocols.task.share.response) {
          return mergeFrom((protocols.task.share.response)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(protocols.task.share.response other) {
        if (other == protocols.task.share.response.getDefaultInstance()) return this;
        if (other.getError() != 0) {
          setError(other.getError());
        }
        if (!other.getErrDesc().isEmpty()) {
          errDesc_ = other.errDesc_;
          onChanged();
        }
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        protocols.task.share.response parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (protocols.task.share.response) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int error_ ;
      /**
       * <pre>
       * 0 = 成功 其他 = 错误
       * </pre>
       *
       * <code>uint32 error = 1;</code>
       */
      public int getError() {
        return error_;
      }
      /**
       * <pre>
       * 0 = 成功 其他 = 错误
       * </pre>
       *
       * <code>uint32 error = 1;</code>
       */
      public Builder setError(int value) {
        
        error_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 0 = 成功 其他 = 错误
       * </pre>
       *
       * <code>uint32 error = 1;</code>
       */
      public Builder clearError() {
        
        error_ = 0;
        onChanged();
        return this;
      }

      private java.lang.Object errDesc_ = "";
      /**
       * <pre>
       * 错误描述
       * </pre>
       *
       * <code>string errDesc = 2;</code>
       */
      public java.lang.String getErrDesc() {
        java.lang.Object ref = errDesc_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          errDesc_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       * 错误描述
       * </pre>
       *
       * <code>string errDesc = 2;</code>
       */
      public com.google.protobuf.ByteString
          getErrDescBytes() {
        java.lang.Object ref = errDesc_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          errDesc_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * 错误描述
       * </pre>
       *
       * <code>string errDesc = 2;</code>
       */
      public Builder setErrDesc(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        errDesc_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 错误描述
       * </pre>
       *
       * <code>string errDesc = 2;</code>
       */
      public Builder clearErrDesc() {
        
        errDesc_ = getDefaultInstance().getErrDesc();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 错误描述
       * </pre>
       *
       * <code>string errDesc = 2;</code>
       */
      public Builder setErrDescBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        errDesc_ = value;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:response)
    }

    // @@protoc_insertion_point(class_scope:response)
    private static final protocols.task.share.response DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new protocols.task.share.response();
    }

    public static protocols.task.share.response getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<response>
        PARSER = new com.google.protobuf.AbstractParser<response>() {
      public response parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new response(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<response> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<response> getParserForType() {
      return PARSER;
    }

    public protocols.task.share.response getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_request_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_request_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_response_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_response_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\020task/share.proto\"\035\n\007request\022\022\n\nshareTi" +
      "mes\030\001 \001(\r\"*\n\010response\022\r\n\005error\030\001 \001(\r\022\017\n\007" +
      "errDesc\030\002 \001(\tB\027\n\016protocols.taskB\005shareb\006" +
      "proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_request_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_request_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_request_descriptor,
        new java.lang.String[] { "ShareTimes", });
    internal_static_response_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_response_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_response_descriptor,
        new java.lang.String[] { "Error", "ErrDesc", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}

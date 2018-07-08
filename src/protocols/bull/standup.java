// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: bull/standup.proto

package protocols.bull;

public final class standup {
  private standup() {}
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
      return protocols.bull.standup.internal_static_request_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return protocols.bull.standup.internal_static_request_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              protocols.bull.standup.request.class, protocols.bull.standup.request.Builder.class);
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
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof protocols.bull.standup.request)) {
        return super.equals(obj);
      }
      protocols.bull.standup.request other = (protocols.bull.standup.request) obj;

      boolean result = true;
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static protocols.bull.standup.request parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protocols.bull.standup.request parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protocols.bull.standup.request parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protocols.bull.standup.request parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protocols.bull.standup.request parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static protocols.bull.standup.request parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static protocols.bull.standup.request parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static protocols.bull.standup.request parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static protocols.bull.standup.request parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static protocols.bull.standup.request parseFrom(
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
    public static Builder newBuilder(protocols.bull.standup.request prototype) {
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
        protocols.bull.standup.requestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return protocols.bull.standup.internal_static_request_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return protocols.bull.standup.internal_static_request_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                protocols.bull.standup.request.class, protocols.bull.standup.request.Builder.class);
      }

      // Construct using protocols.bull.standup.request.newBuilder()
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
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return protocols.bull.standup.internal_static_request_descriptor;
      }

      public protocols.bull.standup.request getDefaultInstanceForType() {
        return protocols.bull.standup.request.getDefaultInstance();
      }

      public protocols.bull.standup.request build() {
        protocols.bull.standup.request result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public protocols.bull.standup.request buildPartial() {
        protocols.bull.standup.request result = new protocols.bull.standup.request(this);
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
        if (other instanceof protocols.bull.standup.request) {
          return mergeFrom((protocols.bull.standup.request)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(protocols.bull.standup.request other) {
        if (other == protocols.bull.standup.request.getDefaultInstance()) return this;
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
        protocols.bull.standup.request parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (protocols.bull.standup.request) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
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
    private static final protocols.bull.standup.request DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new protocols.bull.standup.request();
    }

    public static protocols.bull.standup.request getDefaultInstance() {
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

    public protocols.bull.standup.request getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  public interface responseOrBuilder extends
      // @@protoc_insertion_point(interface_extends:response)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * 错误代码，0=成功 其他=错误
     * </pre>
     *
     * <code>sint32 error = 1;</code>
     */
    int getError();

    /**
     * <pre>
     * 座位ID
     * </pre>
     *
     * <code>uint32 id = 2;</code>
     */
    int getId();

    /**
     * <pre>
     * UID
     * </pre>
     *
     * <code>uint32 uid = 3;</code>
     */
    int getUid();

    /**
     * <pre>
     * 错误描述
     * </pre>
     *
     * <code>string errDesc = 4;</code>
     */
    java.lang.String getErrDesc();
    /**
     * <pre>
     * 错误描述
     * </pre>
     *
     * <code>string errDesc = 4;</code>
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
      id_ = 0;
      uid_ = 0;
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

              error_ = input.readSInt32();
              break;
            }
            case 16: {

              id_ = input.readUInt32();
              break;
            }
            case 24: {

              uid_ = input.readUInt32();
              break;
            }
            case 34: {
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
      return protocols.bull.standup.internal_static_response_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return protocols.bull.standup.internal_static_response_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              protocols.bull.standup.response.class, protocols.bull.standup.response.Builder.class);
    }

    public static final int ERROR_FIELD_NUMBER = 1;
    private int error_;
    /**
     * <pre>
     * 错误代码，0=成功 其他=错误
     * </pre>
     *
     * <code>sint32 error = 1;</code>
     */
    public int getError() {
      return error_;
    }

    public static final int ID_FIELD_NUMBER = 2;
    private int id_;
    /**
     * <pre>
     * 座位ID
     * </pre>
     *
     * <code>uint32 id = 2;</code>
     */
    public int getId() {
      return id_;
    }

    public static final int UID_FIELD_NUMBER = 3;
    private int uid_;
    /**
     * <pre>
     * UID
     * </pre>
     *
     * <code>uint32 uid = 3;</code>
     */
    public int getUid() {
      return uid_;
    }

    public static final int ERRDESC_FIELD_NUMBER = 4;
    private volatile java.lang.Object errDesc_;
    /**
     * <pre>
     * 错误描述
     * </pre>
     *
     * <code>string errDesc = 4;</code>
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
     * <code>string errDesc = 4;</code>
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
        output.writeSInt32(1, error_);
      }
      if (id_ != 0) {
        output.writeUInt32(2, id_);
      }
      if (uid_ != 0) {
        output.writeUInt32(3, uid_);
      }
      if (!getErrDescBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 4, errDesc_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (error_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeSInt32Size(1, error_);
      }
      if (id_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(2, id_);
      }
      if (uid_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(3, uid_);
      }
      if (!getErrDescBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, errDesc_);
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
      if (!(obj instanceof protocols.bull.standup.response)) {
        return super.equals(obj);
      }
      protocols.bull.standup.response other = (protocols.bull.standup.response) obj;

      boolean result = true;
      result = result && (getError()
          == other.getError());
      result = result && (getId()
          == other.getId());
      result = result && (getUid()
          == other.getUid());
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
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + getId();
      hash = (37 * hash) + UID_FIELD_NUMBER;
      hash = (53 * hash) + getUid();
      hash = (37 * hash) + ERRDESC_FIELD_NUMBER;
      hash = (53 * hash) + getErrDesc().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static protocols.bull.standup.response parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protocols.bull.standup.response parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protocols.bull.standup.response parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protocols.bull.standup.response parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protocols.bull.standup.response parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static protocols.bull.standup.response parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static protocols.bull.standup.response parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static protocols.bull.standup.response parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static protocols.bull.standup.response parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static protocols.bull.standup.response parseFrom(
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
    public static Builder newBuilder(protocols.bull.standup.response prototype) {
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
        protocols.bull.standup.responseOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return protocols.bull.standup.internal_static_response_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return protocols.bull.standup.internal_static_response_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                protocols.bull.standup.response.class, protocols.bull.standup.response.Builder.class);
      }

      // Construct using protocols.bull.standup.response.newBuilder()
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

        id_ = 0;

        uid_ = 0;

        errDesc_ = "";

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return protocols.bull.standup.internal_static_response_descriptor;
      }

      public protocols.bull.standup.response getDefaultInstanceForType() {
        return protocols.bull.standup.response.getDefaultInstance();
      }

      public protocols.bull.standup.response build() {
        protocols.bull.standup.response result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public protocols.bull.standup.response buildPartial() {
        protocols.bull.standup.response result = new protocols.bull.standup.response(this);
        result.error_ = error_;
        result.id_ = id_;
        result.uid_ = uid_;
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
        if (other instanceof protocols.bull.standup.response) {
          return mergeFrom((protocols.bull.standup.response)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(protocols.bull.standup.response other) {
        if (other == protocols.bull.standup.response.getDefaultInstance()) return this;
        if (other.getError() != 0) {
          setError(other.getError());
        }
        if (other.getId() != 0) {
          setId(other.getId());
        }
        if (other.getUid() != 0) {
          setUid(other.getUid());
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
        protocols.bull.standup.response parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (protocols.bull.standup.response) e.getUnfinishedMessage();
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
       * 错误代码，0=成功 其他=错误
       * </pre>
       *
       * <code>sint32 error = 1;</code>
       */
      public int getError() {
        return error_;
      }
      /**
       * <pre>
       * 错误代码，0=成功 其他=错误
       * </pre>
       *
       * <code>sint32 error = 1;</code>
       */
      public Builder setError(int value) {
        
        error_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 错误代码，0=成功 其他=错误
       * </pre>
       *
       * <code>sint32 error = 1;</code>
       */
      public Builder clearError() {
        
        error_ = 0;
        onChanged();
        return this;
      }

      private int id_ ;
      /**
       * <pre>
       * 座位ID
       * </pre>
       *
       * <code>uint32 id = 2;</code>
       */
      public int getId() {
        return id_;
      }
      /**
       * <pre>
       * 座位ID
       * </pre>
       *
       * <code>uint32 id = 2;</code>
       */
      public Builder setId(int value) {
        
        id_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 座位ID
       * </pre>
       *
       * <code>uint32 id = 2;</code>
       */
      public Builder clearId() {
        
        id_ = 0;
        onChanged();
        return this;
      }

      private int uid_ ;
      /**
       * <pre>
       * UID
       * </pre>
       *
       * <code>uint32 uid = 3;</code>
       */
      public int getUid() {
        return uid_;
      }
      /**
       * <pre>
       * UID
       * </pre>
       *
       * <code>uint32 uid = 3;</code>
       */
      public Builder setUid(int value) {
        
        uid_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * UID
       * </pre>
       *
       * <code>uint32 uid = 3;</code>
       */
      public Builder clearUid() {
        
        uid_ = 0;
        onChanged();
        return this;
      }

      private java.lang.Object errDesc_ = "";
      /**
       * <pre>
       * 错误描述
       * </pre>
       *
       * <code>string errDesc = 4;</code>
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
       * <code>string errDesc = 4;</code>
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
       * <code>string errDesc = 4;</code>
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
       * <code>string errDesc = 4;</code>
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
       * <code>string errDesc = 4;</code>
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
    private static final protocols.bull.standup.response DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new protocols.bull.standup.response();
    }

    public static protocols.bull.standup.response getDefaultInstance() {
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

    public protocols.bull.standup.response getDefaultInstanceForType() {
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
      "\n\022bull/standup.proto\"\t\n\007request\"C\n\010respo" +
      "nse\022\r\n\005error\030\001 \001(\021\022\n\n\002id\030\002 \001(\r\022\013\n\003uid\030\003 " +
      "\001(\r\022\017\n\007errDesc\030\004 \001(\tB\031\n\016protocols.bullB\007" +
      "standupb\006proto3"
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
        new java.lang.String[] { });
    internal_static_response_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_response_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_response_descriptor,
        new java.lang.String[] { "Error", "Id", "Uid", "ErrDesc", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}

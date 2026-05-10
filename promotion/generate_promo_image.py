#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
ThinkBootCloud 推广图片生成脚本
使用 Pillow 直接生成推广头图（1200x630）
"""

from PIL import Image, ImageDraw, ImageFont
import os
import textwrap

def get_font(size, bold=False):
    """获取字体"""
    font_dir = r"C:\Windows\Fonts"
    if bold:
        font_path = os.path.join(font_dir, "msyhbd.ttc")  # 微软雅黑粗体
    else:
        font_path = os.path.join(font_dir, "msyh.ttc")  # 微软雅黑
    
    try:
        return ImageFont.truetype(font_path, size)
    except:
        return ImageFont.load_default()

def draw_rounded_rectangle(draw, xy, radius, fill):
    """绘制圆角矩形"""
    x0, y0, x1, y1 = xy
    draw.rectangle([x0 + radius, y0, x1 - radius, y1], fill=fill)
    draw.rectangle([x0, y0 + radius, x1, y1 - radius], fill=fill)
    draw.pieslice([x0, y0, x0 + radius * 2, y0 + radius * 2], 180, 270, fill=fill)
    draw.pieslice([x1 - radius * 2, y0, x1, y0 + radius * 2], 270, 360, fill=fill)
    draw.pieslice([x0, y1 - radius * 2, x0 + radius * 2, y1], 90, 180, fill=fill)
    draw.pieslice([x1 - radius * 2, y1 - radius * 2, x1, y1], 0, 90, fill=fill)

def create_promotion_image():
    """创建推广图片"""
    # 图片尺寸
    width, height = 1200, 630
    
    # 创建渐变背景
    img = Image.new('RGB', (width, height))
    pixels = img.load()
    
    # 紫蓝渐变 (#667eea -> #764ba2 -> #f093fb)
    for x in range(width):
        for y in range(height):
            # 计算渐变
            t1 = (x + y) / (width + height)
            t2 = t1 * t1
            
            r = int(102 + (118 - 102) * t1 + (240 - 118) * t2)
            g = int(126 + (75 - 126) * t1 + (147 - 75) * t2)
            b = int(234 + (162 - 234) * t1 + (251 - 162) * t2)
            
            pixels[x, y] = (r, g, b)
    
    draw = ImageDraw.Draw(img)
    
    # 添加背景装饰圆
    draw.ellipse([900, -200, 1700, 600], fill=(255, 255, 255, 25))  # 半透明大圆
    draw.ellipse([-100, 400, 500, 1000], fill=(255, 215, 0, 38))  # 金色圆
    
    # 中心内容起始 y
    center_y = 100
    
    # ===== Logo 区域 =====
    logo_y = center_y
    
    # Logo 图标 (金色圆角方块)
    icon_x, icon_y = 470, logo_y
    icon_size = 80
    draw_rounded_rectangle(draw, [icon_x, icon_y, icon_x + icon_size, icon_y + icon_size], 18, fill=(255, 215, 0))
    
    # 云朵图标文字
    font_icon = get_font(48)
    draw.text((icon_x + 12, icon_y + 10), "☁", fill=(102, 126, 234), font=font_icon)
    
    # Logo 文字
    font_logo = get_font(52, bold=True)
    logo_text_x = icon_x + icon_size + 20
    draw.text((logo_text_x, logo_y + 10), "Think", fill=(255, 255, 255), font=font_logo)
    draw.text((logo_text_x + 235, logo_y + 10), "BootCloud", fill=(255, 215, 0), font=font_logo)
    
    # ===== 主标题 =====
    title_y = logo_y + icon_size + 40
    font_title = get_font(44, bold=True)
    title_text = "还在手写微服务脚手架？"
    title_width = draw.textlength(title_text, font=font_title)
    draw.text(((width - title_width) / 2, title_y), title_text, fill=(255, 255, 255), font=font_title)
    
    highlight_y = title_y + 55
    font_highlight = get_font(44, bold=True)
    highlight_text = "开箱即用 才是正解！"
    highlight_width = draw.textlength(highlight_text, font=font_highlight)
    draw.text(((width - highlight_width) / 2, highlight_y), highlight_text, fill=(255, 215, 0), font=font_highlight)
    
    # ===== 副标题 =====
    subtitle_y = highlight_y + 65
    font_subtitle = get_font(22)
    subtitle_text = "专为 C 端客户端设计的轻量级微服务框架"
    subtitle_width = draw.textlength(subtitle_text, font=font_subtitle)
    draw.text(((width - subtitle_width) / 2, subtitle_y), subtitle_text, fill=(255, 255, 255), font=font_subtitle)
    
    subtitle2_y = subtitle_y + 32
    subtitle2_text = "Spring Boot 3 + Spring Cloud Alibaba · 一行配置即可开发"
    subtitle2_width = draw.textlength(subtitle2_text, font=font_subtitle)
    draw.text(((width - subtitle2_width) / 2, subtitle2_y), subtitle2_text, fill=(230, 230, 230), font=font_subtitle)
    
    # ===== 特性标签 =====
    tags_y = subtitle2_y + 55
    tags = [
        ("⚡ 零配置开箱即用", 0),
        ("🔐 默认安全认证", 250),
        ("🚀 代码一键生成", 510),
        ("📦 模块化按需引入", 780)
    ]
    
    font_tag = get_font(18, bold=True)
    for tag_text, tag_x in tags:
        tag_width = int(draw.textlength(tag_text, font=font_tag))
        tag_padding_x = 25
        tag_padding_y = 12
        tag_rect = [tag_x, tags_y, tag_x + tag_width + tag_padding_x * 2, tags_y + 40]
        
        # 绘制半透明圆角矩形背景
        draw_rounded_rectangle(draw, tag_rect, 20, fill=(255, 255, 255, 50))
        
        # 绘制边框
        draw_rounded_rectangle(draw, tag_rect, 20, fill=None)
        draw.rectangle([tag_rect[0], tag_rect[1], tag_rect[2], tag_rect[3]], outline=(255, 255, 255, 80), width=2)
        
        # 绘制文字
        draw.text((tag_x + tag_padding_x, tags_y + 10), tag_text, fill=(255, 255, 255), font=font_tag)
    
    # ===== 技术栈 =====
    tech_y = tags_y + 60
    tech_items = ["Spring Boot 3.2", "Spring Cloud 2023", "MyBatis-Plus", "Redis", "Nacos"]
    font_tech = get_font(16)
    
    tech_total_width = sum([int(draw.textlength(t, font=font_tech)) for t in tech_items]) + 50 * (len(tech_items) - 1)
    tech_start_x = (width - tech_total_width) // 2
    
    current_x = tech_start_x
    for tech in tech_items:
        tech_width = int(draw.textlength(tech, font=font_tech))
        # 背景
        draw_rounded_rectangle(draw, [current_x, tech_y, current_x + tech_width + 30, tech_y + 30], 15, fill=(255, 255, 255, 25))
        draw.text((current_x + 15, tech_y + 7), tech, fill=(220, 220, 220), font=font_tech)
        current_x += tech_width + 50
    
    # ===== 底部信息 =====
    bottom_y = height - 50
    
    # MIT 标签
    font_protocol = get_font(14, bold=True)
    protocol_text = "MIT"
    protocol_width = int(draw.textlength(protocol_text, font=font_protocol))
    protocol_x = 440
    draw_rounded_rectangle(draw, [protocol_x, bottom_y - 5, protocol_x + protocol_width + 20, bottom_y + 22], 6, fill=(255, 215, 0))
    draw.text((protocol_x + 10, bottom_y), protocol_text, fill=(102, 126, 234), font=font_protocol)
    
    # 底部文字
    font_bottom = get_font(15)
    bottom_text = "完全免费 · 可自由商用"
    bottom_x = protocol_x + protocol_width + 35
    draw.text((bottom_x, bottom_y), bottom_text, fill=(200, 200, 200), font=font_bottom)
    
    # 网址
    font_url = get_font(14)
    url_text = "gitee.com/hongxinge/think-boot-cloud"
    url_width = int(draw.textlength(url_text, font=font_url))
    draw.text(((width - url_width) / 2, bottom_y + 30), url_text, fill=(255, 215, 0), font=font_url)
    
    # 保存
    output_path = os.path.join(os.path.dirname(os.path.dirname(__file__)), 'promotion', 'thinkboot-cloud-promo.png')
    img.save(output_path, 'PNG', quality=95)
    print(f'✅ 推广图片已生成：{output_path}')
    print(f'   尺寸：{width}x{height}')
    return output_path

if __name__ == '__main__':
    create_promotion_image()
